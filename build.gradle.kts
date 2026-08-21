plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "418a1ba"
    id("com.gradleup.shadow") version "9.6.1"
    id("me.modmuss50.mod-publish-plugin") version "675051c"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

group = "xyz.srnyx"
description = "You can't touch any blocks with the chosen color!"

galaxy {
    minecraft {
        spigotAPI("1.8.8")
        annoyingAPI("8402640")

        dependency {
            optional {
                repositories.add(PLACEHOLDER_API)
                group = "me.clip"
                artifact = "placeholderapi"
                version = "2.12.2"

                pluginYml = "PlaceholderAPI"
                modrinth = "placeholderapi"
                hangar = "PlaceholderAPI"
            }
        }

        pluginYml {
            developerData(SRNYX)

            command("colorreload") {
                aliases.add("criticalcolorsreload")
                description = "Relaod the plugin"

                permission("reload")
            }
            command("color") {
                aliases.add("criticalcolors")
                description = "Change the color players can't touch"

                permission("color")
            }
            command("colorrotate") {
                aliases.add("criticalcolorsrotate")
                description = "Toggle the automatic color change"

                permission("rotate")
            }
            command("colorbar") {
                aliases.addAll("colorbossbar", "criticalcolorsbar", "criticalcolorsbossbar")
                description = "Toggle the color bossbar"

                permission("bar")
            }

            permission("bypass") {
                description = "Allows the players to bypass dying from colors"
                default = FALSE
            }
        }

        platformPublishing {
            github("srnyx/critical-colors")
            modrinth("ZXSFfy1U")
            hangar("CriticalColors")
            spigot("107312")
            curseforge("805743")

            projectData("critical-colors")
        }
    }
}
