/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.command.submission

import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import io.github.reflxction.snowy.util.PluginIdentifier
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.Permission

@BotCommand(
        name = "release",
        parameters = "<plugin> <release url> <version> <title>",
        minimumArguments = 4,
        category = CommandCategory.ADMIN,
        requiredPermissions = [Permission.ADMINISTRATOR],
        description = "Post a plugin update",
        help = false
)
object ReleaseCommand : CommandCallback {

    private const val RELEASE_URL = "https://www.spigotmc.org/resources/73093/update?update="

    private const val FEED_CHANNEL = "700784039409483817"

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val plugin = context.resolve(0, PluginIdentifier::class.java)
        val release = RELEASE_URL + context.args[1].split("update?update=")[1]
        val title = context.join(3)
        val summary = title.substring(0, if (title.contains("(")) title.lastIndexOf("(") else title.length).trim { it <= ' ' }
        val notes: String? = if (title.contains("(")) title.substring(title.lastIndexOf("(") + 1, title.indexOf(")")) else null
        val embed = EmbedBuilder()
                .setTitle(plugin.name)
                .setColor(plugin.color)
                .setDescription("**${context.args[2]}** is now available!")
                .addField("Summary", summary, false)
                .addField("Full changelog", release, false)
        if (notes != null)
            embed.addField("Note", notes, false)
        context.event.guild.getTextChannelById(FEED_CHANNEL)?.sendMessage(MessageBuilder().setEmbed(embed.build()).setContent("@everyone").build())?.queue()
    }

}