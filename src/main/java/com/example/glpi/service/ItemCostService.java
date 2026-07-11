package com.example.glpi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.glpi.dto.CostItemDTO;
import com.example.glpi.dto.ItemCostDTO;
import com.example.glpi.dto.ItemGroup;
import com.example.glpi.dto.ItemTypeCostDTO;
import com.example.glpi.dto.TicketCostDTO;
import com.example.glpi.entity.CostType;
import com.example.glpi.entity.ItemCost;
import com.example.glpi.entity.Plafond;
import com.example.glpi.enums.CostTypeEnum;
import com.example.glpi.enums.ModeEnum;
import com.example.glpi.repository.ItemCostRepository;
import com.example.glpi.repository.PlafondRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemCostService {

    private final ItemCostRepository itemCostRepository;
    private final PlafondRepository plafondRepository;

    public Double getPlafond() {
        List<Plafond> plafonds = plafondRepository.findAll();
        System.out.println(plafonds.get(plafonds.size() - 1).getPlafond());
        return plafonds.get(plafonds.size() - 1).getPlafond();
    }

    public void setPlafond(Double plafond) {
        Plafond plafond2 = new Plafond();
        plafond2.setPlafond(plafond);
        plafondRepository.save(plafond2);
    }

    private Map<Integer, Double> toMap(List<ItemCost> costs) {
        if (costs == null)
            return new HashMap<Integer, Double>();
        return costs.stream()
                .collect(Collectors.toMap(
                        ItemCost::getItemId,
                        ItemCost::getCost));
    }

    private Map<Integer, Double> toGroupMap(List<ItemGroup> groups) {
        if (groups == null)
            return new HashMap<Integer, Double>();
        return groups.stream()
                .collect(Collectors.toMap(
                        ItemGroup::getItemId,
                        ItemGroup::getCost));
    }

    public void saveItemCost(List<ItemCostDTO> dtos) {
        if (dtos.size() == 0)
            return;

        Integer superCostTypeId = CostTypeEnum.SUPER_COST.getId();
        Integer openCostTypeId = CostTypeEnum.OPEN_COST.getId();

        Integer lastGroupId = itemCostRepository.findLastGroupIdByTicketId(dtos.get(0).getTicketId());

        if (lastGroupId == null)
            lastGroupId = 0;

        lastGroupId++;

        Map<Integer, Double> lastItemCosts = toMap(
                itemCostRepository.findLastItemCostsByTicketId(dtos.get(0).getTicketId(),
                        superCostTypeId));
        Map<Integer, Double> firstItemCosts = toMap(
                itemCostRepository.findFirstItemCostsByTicketId(dtos.get(0).getTicketId(),
                        superCostTypeId));
        Map<Integer, Double> itemCostsSum = toGroupMap(
                itemCostRepository.findItemCostsSumByTicketId(dtos.get(0).getTicketId(),
                        superCostTypeId));
        Map<Integer, Double> itemCostsAvg = toGroupMap(
                itemCostRepository.findItemCostsAvgByTicketId(dtos.get(0).getTicketId(),
                        superCostTypeId));
        double sumOpenValue = itemCostRepository.findValueByTicketId(dtos.get(0).getTicketId(),
                openCostTypeId, lastGroupId);

        System.out.println(getPlafond());

        for (ItemCostDTO dto : dtos) {
            ItemCost cost = null;
            double superCostTotal = 0;
            if (dto.getType().equalsIgnoreCase("open")) {

                if (dto.getMode() == ModeEnum.LAST_SUPER_COST.getId())
                    superCostTotal = lastItemCosts.getOrDefault(dto.getItemId(), 0.0);
                else if (dto.getMode() == ModeEnum.FIRST_SUPER_COST.getId())
                    superCostTotal = firstItemCosts.getOrDefault(dto.getItemId(), 0.0);
                else if (dto.getMode() == ModeEnum.AVG_SUPER_COST.getId())
                    superCostTotal = itemCostsAvg.getOrDefault(dto.getItemId(), 0.0);
                else if (dto.getMode() == ModeEnum.SUM_SUPER_COST.getId())
                    superCostTotal = itemCostsSum.getOrDefault(dto.getItemId(), 0.0);

                double openCost = (superCostTotal * dto.getValue()) / 100;

                if (sumOpenValue + dto.getValue() > getPlafond()) {
                    openCost = 0;
                }

                cost = buildItemCost(dto, lastGroupId, openCostTypeId, openCost);

            } else if (dto.getType().equalsIgnoreCase("close")) {
                cost = buildItemCost(dto, lastGroupId, superCostTypeId, dto.getValue());
            }
            itemCostRepository.save(cost);
        }

    }

    private ItemCost buildItemCost(ItemCostDTO dto, Integer groupId, Integer typeId, Double cost) {
        ItemCost itemCost = new ItemCost();

        CostType type = new CostType();
        type.setId(typeId);

        itemCost.setCost(cost);
        itemCost.setCostType(type);

        itemCost.setTicketId(dto.getTicketId());
        itemCost.setItemId(dto.getItemId());
        itemCost.setItemType(dto.getItemType());
        itemCost.setMode(dto.getMode());
        itemCost.setValue(dto.getValue());
        itemCost.setGroupId(groupId);
        itemCost.setDelete(false);

        return itemCost;
    }

    public void cancelLastSuperCost(Integer ticketId) {
        Integer superCostTypeId = CostTypeEnum.SUPER_COST.getId();

        List<ItemCost> itemCosts = itemCostRepository.findLastItemCostsByTicketId(ticketId, superCostTypeId);
        for (ItemCost itemCost : itemCosts) {
            itemCost.setDelete(true);
            itemCostRepository.save(itemCost);
        }
    }

    public void retablirLastSuperCost(Integer ticketId, Integer groupId) {
        List<ItemCost> itemCostsCancel = itemCostRepository.findCancelItemCostSuper(ticketId, groupId);
        for (ItemCost itemCost : itemCostsCancel) {
            itemCost.setDelete(false);
            itemCostRepository.save(itemCost);
        }
        List<ItemCost> itemCosts = itemCostRepository.findBySupGroupIdAndTicketIdAndCostTypeId(
                ticketId, 2, groupId);
        for (ItemCost itemCost : itemCosts) {
            List<ItemCost> lastItemCosts = itemCostRepository.findLastItemCostsByTicketIdAndGroupId(
                    itemCost.getTicketId(),
                    1, itemCost.getGroupId());
            List<ItemCost> firstItemCosts = itemCostRepository.findFirstItemCostsByTicketIdAndGroupId(
                    itemCost.getTicketId(),
                    1, itemCost.getGroupId());
            List<ItemGroup> itemCostsSum = itemCostRepository.findItemCostsSumByTicketIdAndGroupId(
                    itemCost.getTicketId(),
                    1, itemCost.getGroupId());
            List<ItemGroup> itemCostsAvg = itemCostRepository.findItemCostsAvgByTicketIdAndGroupId(
                    itemCost.getTicketId(),
                    1, itemCost.getGroupId());
            double superCostTotal = 0.0;
            int size = 1;
            double sumOpenValue = itemCostRepository.findValueByTicketId(itemCost.getTicketId(), 2,
                    itemCost.getGroupId());
            if (itemCost.getMode() == ModeEnum.LAST_SUPER_COST.getId()) {
                if (lastItemCosts != null && lastItemCosts.size() != 0) {
                    superCostTotal = lastItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                    size = lastItemCosts.size();
                }
            }
            if (itemCost.getMode() == ModeEnum.FIRST_SUPER_COST.getId()) {
                if (firstItemCosts != null && firstItemCosts.size() != 0) {
                    superCostTotal = firstItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                    size = firstItemCosts.size();
                }
            }
            if (itemCost.getMode() == ModeEnum.AVG_SUPER_COST.getId()) {
                if (itemCostsAvg != null && itemCostsAvg.size() != 0) {
                    superCostTotal = itemCostsAvg.stream().mapToDouble(ItemGroup::getCost).sum();
                    size = itemCostsAvg.size();
                }
            }
            if (itemCost.getMode() == ModeEnum.SUM_SUPER_COST.getId()) {
                if (itemCostsSum != null && itemCostsSum.size() != 0) {
                    superCostTotal = itemCostsSum.stream().mapToDouble(ItemGroup::getCost).sum();
                    size = itemCostsSum.size();
                }
            }
            double openCost = (superCostTotal * itemCost.getValue()) / 100;
            if (sumOpenValue + itemCost.getValue() > getPlafond()) {
                openCost = 0;
            }
            openCost /= size;
            itemCost.setCost(openCost);

            itemCostRepository.save(itemCost);
        }
    }

    public Map<String, ItemTypeCostDTO> getCostDTOs() {
        Integer superCostTypeId = CostTypeEnum.SUPER_COST.getId();
        Integer openCostTypeId = CostTypeEnum.OPEN_COST.getId();

        List<ItemCost> itemCosts = itemCostRepository.findAll();
        Map<String, ItemTypeCostDTO> itemTypeCostDTOs = new HashMap<>();

        for (ItemCost itemCost : itemCosts) {
            String key = itemCost.getItemType();
            if (!itemTypeCostDTOs.containsKey(key)) {
                ItemTypeCostDTO itemTypeCostDTO = new ItemTypeCostDTO();
                itemTypeCostDTO.setType(key);
                itemTypeCostDTO.setSuperCost(0.);
                itemTypeCostDTO.setOpenCost(0.);
                itemTypeCostDTOs.put(key, itemTypeCostDTO);
            }

            ItemTypeCostDTO itemTypeCostDTO = itemTypeCostDTOs.get(key);
            if (itemCost.getCostType() != null
                    && itemCost.getCostType().getId() == superCostTypeId) {
                double superCost = itemTypeCostDTO.getSuperCost();
                if (itemCost.getCost() != null && itemCost.isDelete() == false) {
                    superCost += itemCost.getCost();
                    itemTypeCostDTO.setSuperCost(superCost);
                }
            }

            if (itemCost.getCostType() != null
                    && itemCost.getCostType().getId() == openCostTypeId) {
                double openCost = itemTypeCostDTO.getOpenCost();
                if (itemCost.getCost() != null) {
                    openCost += itemCost.getCost();
                    itemTypeCostDTO.setOpenCost(openCost);
                }
            }

        }
        return itemTypeCostDTOs;
    }

    public List<CostItemDTO> getDetailsByItemType(String itemType) {
        List<CostItemDTO> details = itemCostRepository.findByItemType(itemType);
        return details;
    }

    public void reset() {
        itemCostRepository.deleteAll();
    }

    public List<TicketCostDTO> getTicketCostDTOs() {
        return itemCostRepository.findTicketCosts();
    }

    public List<TicketCostDTO> getTicketCancelCostDTOs() {
        return itemCostRepository.findTicketCancelCosts();
    }

    public void updateTicketCost(TicketCostDTO ticketCostDTO) {

        if (ticketCostDTO.getCostTypeId() == 1) {

            List<Integer> ids = itemCostRepository.findIdByGroupAndTicketAndCostType(
                    ticketCostDTO.getGroupId(),
                    ticketCostDTO.getTicketId(), 1);
            for (Integer id : ids) {
                ItemCost itemCost = itemCostRepository.findById(id).orElseThrow();
                itemCost.setCost(ticketCostDTO.getValue() / ids.size());
                itemCost.setValue(ticketCostDTO.getValue() / ids.size());
                itemCostRepository.save(itemCost);
            }
            List<ItemCost> itemCosts = itemCostRepository.findBySupGroupIdAndTicketIdAndCostTypeId(
                    ticketCostDTO.getTicketId(), 2, ticketCostDTO.getGroupId());
            for (ItemCost itemCost : itemCosts) {
                List<ItemCost> lastItemCosts = itemCostRepository.findLastItemCostsByTicketIdAndGroupId(
                        itemCost.getTicketId(),
                        1, itemCost.getGroupId());
                List<ItemCost> firstItemCosts = itemCostRepository.findFirstItemCostsByTicketIdAndGroupId(
                        itemCost.getTicketId(),
                        1, itemCost.getGroupId());
                List<ItemGroup> itemCostsSum = itemCostRepository.findItemCostsSumByTicketIdAndGroupId(
                        itemCost.getTicketId(),
                        1, itemCost.getGroupId());
                List<ItemGroup> itemCostsAvg = itemCostRepository.findItemCostsAvgByTicketIdAndGroupId(
                        itemCost.getTicketId(),
                        1, itemCost.getGroupId());
                double superCostTotal = 0.0;
                int size = 1;
                double sumOpenValue = itemCostRepository.findValueByTicketId(itemCost.getTicketId(), 2,
                        itemCost.getGroupId());

                if (itemCost.getMode() == 1) {
                    if (lastItemCosts != null && lastItemCosts.size() != 0) {
                        superCostTotal = lastItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                        size = lastItemCosts.size();
                    }
                }
                if (itemCost.getMode() == 2) {
                    if (firstItemCosts != null && firstItemCosts.size() != 0) {
                        superCostTotal = firstItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                        size = firstItemCosts.size();
                    }
                }
                if (itemCost.getMode() == 3) {
                    if (itemCostsAvg != null && itemCostsAvg.size() != 0) {
                        superCostTotal = itemCostsAvg.stream().mapToDouble(ItemGroup::getCost).sum();
                        size = itemCostsAvg.size();
                    }
                }
                if (itemCost.getMode() == 4) {
                    if (itemCostsSum != null && itemCostsSum.size() != 0) {
                        superCostTotal = itemCostsSum.stream().mapToDouble(ItemGroup::getCost).sum();
                        size = itemCostsSum.size();
                    }
                }
                double openCost = (superCostTotal * itemCost.getValue()) / 100;
                if (sumOpenValue + itemCost.getValue() > getPlafond()) {
                    openCost = 0;
                }
                openCost /= size;
                itemCost.setCost(openCost);

                itemCostRepository.save(itemCost);
            }
        } else {
            List<Integer> ids = itemCostRepository.findIdByGroupAndTicketAndCostType(
                    ticketCostDTO.getGroupId(),
                    ticketCostDTO.getTicketId(), 2);
            List<ItemCost> lastItemCosts = itemCostRepository.findLastItemCostsByTicketIdAndGroupId(
                    ticketCostDTO.getTicketId(),
                    1, ticketCostDTO.getGroupId());
            List<ItemCost> firstItemCosts = itemCostRepository.findFirstItemCostsByTicketIdAndGroupId(
                    ticketCostDTO.getTicketId(),
                    1, ticketCostDTO.getGroupId());
            List<ItemGroup> itemCostsSum = itemCostRepository.findItemCostsSumByTicketIdAndGroupId(
                    ticketCostDTO.getTicketId(),
                    1, ticketCostDTO.getGroupId());
            List<ItemGroup> itemCostsAvg = itemCostRepository.findItemCostsAvgByTicketIdAndGroupId(
                    ticketCostDTO.getTicketId(),
                    1, ticketCostDTO.getGroupId());
            double sumOpenValue = itemCostRepository.findValueByTicketId(ticketCostDTO.getTicketId(), 2,
                        ticketCostDTO.getGroupId());
            for (Integer id : ids) {
                ItemCost itemCost = itemCostRepository.findById(id).orElseThrow();
                itemCost.setMode(ticketCostDTO.getMode());
                itemCost.setValue(ticketCostDTO.getValue());
                double superCostTotal = 0.0;
                int size = 1;

                if (ticketCostDTO.getMode() == 1) {
                    if (lastItemCosts != null && lastItemCosts.size() != 0) {
                        superCostTotal = lastItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                        size = lastItemCosts.size();
                    }
                }
                if (ticketCostDTO.getMode() == 2) {
                    if (firstItemCosts != null && firstItemCosts.size() != 0) {
                        superCostTotal = firstItemCosts.stream().mapToDouble(ItemCost::getCost).sum();
                        size = firstItemCosts.size();
                    }
                }
                if (ticketCostDTO.getMode() == 3) {
                    if (itemCostsAvg != null && itemCostsAvg.size() != 0) {
                        superCostTotal = itemCostsAvg.stream().mapToDouble(ItemGroup::getCost).sum();
                        size = itemCostsAvg.size();
                    }
                }
                if (ticketCostDTO.getMode() == 4) {
                    if (itemCostsSum != null && itemCostsSum.size() != 0) {
                        superCostTotal = itemCostsSum.stream().mapToDouble(ItemGroup::getCost).sum();
                        size = itemCostsSum.size();
                    }
                }

                double openCost = (superCostTotal * ticketCostDTO.getValue()) / 100;
                if (sumOpenValue + ticketCostDTO.getValue() > getPlafond()) {
                    openCost = 0;
                }
                openCost /= size;
                itemCost.setCost(openCost);

                itemCostRepository.save(itemCost);
            }
        }

    }
}
