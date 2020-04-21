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
import net.dv8tion.jda.api.utils.MarkdownUtil

@BotCommand(
        name = "suggest",
        description = "Submit a suggestion",
        parameters = "<plugin> <suggestion description>",
        category = CommandCategory.CONTACT_US,
        aliases = ["suggestion"],
        minimumArguments = 2
)
object SuggestCommand : CommandCallback {

    const val YES_EMOTE = "468484613992480788"

    const val NO_EMOTE = "412955116459655169"

    private const val   CHANNEL_ID: String = "700784071441383474"

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val plugin = context.resolve(0, PluginIdentifier::class.java)
        val desc = MarkdownUtil.codeblock("css", context.join(1))
        val channel = context.event.guild.getTextChannelById(CHANNEL_ID)
        val msg = channel!!.sendMessage(EmbedBuilder()
                .setTitle("Suggestion")
                .setAuthor(context.event.member?.effectiveName)
                .setThumbnail(context.event.author.avatarUrl)
                .setColor(-82414)
                .addField("Plugin", plugin.name, false)
                .addField("Suggestion description", desc.plus("\n \n \n"), false)
                .setFooter("To submit a suggestion, run !suggest <plugin> <description> in the bots channel.")
                .build())
                .complete(true)
        context.reply("Suggestion submitted.")
        val boop = context.event.jda.getGuildById("285409136995336192") ?: return
        msg.addReaction(boop.getEmoteById(YES_EMOTE)!!).queue()
        msg.addReaction(boop.getEmoteById(NO_EMOTE)!!).queue()
    }
}