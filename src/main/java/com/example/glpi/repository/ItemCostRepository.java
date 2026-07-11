package com.example.glpi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.glpi.dto.CostItemDTO;
import com.example.glpi.dto.ItemGroup;
import com.example.glpi.dto.TicketCostDTO;
import com.example.glpi.entity.ItemCost;

public interface ItemCostRepository extends JpaRepository<ItemCost, Integer> {

    @Query("""
                SELECT i FROM ItemCost i
                    WHERE i.groupId = (
                        SELECT MAX(i2.groupId)
                            FROM ItemCost i2
                            WHERE i2.ticketId = :ticketId
                    )
                    AND i.ticketId = :ticketId
                    AND i.costType.id = :typeId
                    AND i.isDelete = FALSE
            """)
    List<ItemCost> findLastItemCostsByTicketId(Integer ticketId, Integer typeId);

    @Query("""
          SELECT i FROM ItemCost i
                    WHERE i.groupId = (
                        SELECT MAX(i2.groupId)
                            FROM ItemCost i2
                            WHERE i2.ticketId = :ticketId
                            AND i2.groupId < :groupId
                    )
                    AND i.ticketId = :ticketId
                    AND i.costType.id = :typeId  
                    AND i.isDelete = FALSE
    """)
    List<ItemCost> findLastItemCostsByTicketIdAndGroupId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("""
                SELECT i FROM ItemCost i
                    WHERE i.groupId = (
                        SELECT MIN(i2.groupId)
                            FROM ItemCost i2
                            WHERE i2.ticketId = :ticketId
                    )
                    AND i.ticketId = :ticketId
                    AND i.costType.id = :typeId
                    AND i.isDelete = FALSE
            """)
    List<ItemCost> findFirstItemCostsByTicketId(Integer ticketId, Integer typeId);

    @Query("""
          SELECT i FROM ItemCost i
                    WHERE i.groupId = (
                        SELECT MIN(i2.groupId)
                            FROM ItemCost i2
                            WHERE i2.ticketId = :ticketId
                            AND i2.groupId < :groupId
                    )
                    AND i.ticketId = :ticketId
                    AND i.costType.id = :typeId  
                    AND i.isDelete = FALSE
    """)
    List<ItemCost> findFirstItemCostsByTicketIdAndGroupId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("""
        SELECT i.itemId AS itemId, SUM(i.cost) AS cost  FROM ItemCost i WHERE i.ticketId = :ticketId AND i.costType.id = :typeId AND i.isDelete = FALSE GROUP BY i.itemId
    """)
    List<ItemGroup> findItemCostsSumByTicketId(Integer ticketId, Integer typeId);

    @Query("""
                SELECT SUM(i.value) FROM ItemCost i WHERE i.ticketId = :ticketId AND i.costType.id = :typeId AND i.isDelete = FALSE AND i.groupId < :groupId
            """)
    Double findValueByTicketId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("""
        SELECT i.itemId AS itemId, SUM(i.cost) AS cost  FROM ItemCost i WHERE i.ticketId = :ticketId AND i.costType.id = :typeId AND i.groupId < :groupId AND i.isDelete = FALSE GROUP BY i.itemId
    """)
    List<ItemGroup> findItemCostsSumByTicketIdAndGroupId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("""
                SELECT i.itemId AS itemId, AVG(i.cost) AS cost FROM ItemCost i WHERE i.ticketId = :ticketId AND i.costType.id = :typeId AND i.isDelete = FALSE GROUP BY i.itemId
            """)
    List<ItemGroup> findItemCostsAvgByTicketId(Integer ticketId, Integer typeId);

    @Query("""
        SELECT i.itemId AS itemId, AVG(i.cost) AS cost  FROM ItemCost i WHERE i.ticketId = :ticketId AND i.costType.id = :typeId AND i.groupId < :groupId AND i.isDelete = FALSE GROUP BY i.itemId
    """)
    List<ItemGroup> findItemCostsAvgByTicketIdAndGroupId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("SELECT MAX(i.groupId) FROM ItemCost i WHERE i.ticketId = :ticketId")
    Integer findLastGroupIdByTicketId(Integer ticketId);

    @Query("""
            SELECT i.itemId AS itemId,
            SUM(
                CASE
                    WHEN i.costType.name = 'OPEN_COST'
                    THEN i.cost
                    ELSE 0
                END
            ) AS open_cost,

            SUM(
                CASE
                    WHEN i.costType.name = 'SUPER_COST'
                    THEN i.cost
                    ELSE 0
                END
            ) AS super_cost

            FROM ItemCost i  WHERE i.itemType = :itemType AND i.isDelete = FALSE
            GROUP BY i.itemId
            """)
    List<CostItemDTO> findByItemType(String itemType);

    @Query("""
            SELECT i.ticketId AS ticketId,
                i.groupId AS groupId,
                i.costType.id AS costTypeId,
                SUM(i.cost) AS cost,
                CASE
                    WHEN i.costType.name = 'OPEN_COST'
                    THEN i.value
                    ELSE SUM(i.value)
                END
                AS value,
                i.mode AS mode
            FROM ItemCost i WHERE i.isDelete = FALSE
                GROUP BY i.ticketId, i.groupId, i.costType.id
            """)
    List<TicketCostDTO> findTicketCosts();

    @Query("""
            SELECT i.ticketId AS ticketId,
                i.groupId AS groupId,
                i.costType.id AS costTypeId,
                SUM(i.cost) AS cost,
                SUM(i.value) AS value,
                i.mode AS mode
            FROM ItemCost i WHERE i.isDelete = TRUE
                GROUP BY i.ticketId, i.groupId, i.costType.id
            """)
    List<TicketCostDTO> findTicketCancelCosts();

    @Query("""
        SELECT i.id FROM ItemCost i WHERE i.groupId = :groupId AND i.ticketId = :ticketId AND i.costType.id = :costTypeId
    """)
    List<Integer> findIdByGroupAndTicketAndCostType(Integer groupId, Integer ticketId, Integer costTypeId);

    @Query("""
        SELECT i FROM ItemCost i WHERE i.groupId > :groupId AND i.ticketId = :ticketId AND i.costType.id = :typeId AND i.isDelete = FALSE
    """)
    List<ItemCost> findBySupGroupIdAndTicketIdAndCostTypeId(Integer ticketId, Integer typeId, Integer groupId);

    @Query("""
        SELECT i FROM ItemCost i WHERE i.ticketId = :ticketId AND i.groupId = :groupId AND i.costType.id = 1
    """)
    List<ItemCost> findCancelItemCostSuper(Integer ticketId, Integer groupId); 
}
