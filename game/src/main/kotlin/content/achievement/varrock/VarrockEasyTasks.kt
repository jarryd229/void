package content.achievement.varrock

import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas


class VarrockEasyTasks : Script {

    init {

        interfaceClosed("thessalias_makeovers") {
            set("strike_a_pose_task", true)
        }

        itemAdded("iron_ore", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["varrock_south_east_mine"]) {
                set("doing_the_ironing_task", true)
            }
        }

    }
}
