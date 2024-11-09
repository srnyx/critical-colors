import xyz.srnyx.gradlegalaxy.utility.setupAnnoyingAPI
import xyz.srnyx.gradlegalaxy.utility.spigotAPI
import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "1.3.2"
    id("com.gradleup.shadow") version "8.3.5"
}

setupAnnoyingAPI("5.1.3", "xyz.srnyx", "2.1.0", "You can't touch any blocks with the chosen color!")
spigotAPI("1.8.8")

repository(Repository.PLACEHOLDER_API)
dependencies.compileOnly("me.clip", "placeholderapi", "2.11.3")
