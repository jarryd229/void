package content.area.asgarnia.goblin_village

import content.entity.player.dialogue.type.item
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory

class GoblinVillage : Script {

    init {

        objectOperate("Search", "crate_142") { (target) ->
            if (!get("gobdip_crate1_searched", false)) {
                if (inventory.isFull()) {
                    item("goblin_mail",600,"You find some goblin mail in the crate, but you don't have enough room to take it.")
                    return@objectOperate
                }
                inventory.add("goblin_mail")
                set("gobdip_crate1_searched", true)
                item("goblin_mail",600,"You find some goblin mail in the crate.")
            } else {
                message("You search the crate, but find nothing of interest.")
            }
        }

        objectOperate("Search", "crate_143") { (target) ->
            if (!get("gobdip_crate2_searched", false)) {
                if (inventory.isFull()) {
                    item("goblin_mail",600,"You find some goblin mail in the crate, but you don't have enough room to take it.")
                    return@objectOperate
                }
                inventory.add("goblin_mail")
                set("gobdip_crate2_searched", true)
                item("goblin_mail",600,"You find some goblin mail in the crate.")
            } else {
                message("You search the crate, but find nothing of interest.")
            }
        }

        objectOperate("Search", "crate_144") { (target) ->
            if (!get("gobdip_crate3_searched", false)) {
                if (inventory.isFull()) {
                    item("goblin_mail",600,"You find some goblin mail in the crate, but you don't have enough room to take it.")
                    return@objectOperate
                }
                inventory.add("goblin_mail")
                set("gobdip_crate3_searched", true)
                item("goblin_mail",600,"You find some goblin mail in the crate.")
            } else {
                message("You search the crate, but find nothing of interest.")
            }
        }
    }
}
