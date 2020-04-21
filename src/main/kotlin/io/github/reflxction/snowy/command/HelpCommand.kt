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
package io.github.reflxction.snowy.command

import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.core.*
import io.github.reflxction.snowy.util.Paginator
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction

@BotCommand(
        name = "help",
        description = "Get a list of all bot commands",
        parameters = "[command]",
        category = CommandCategory.USER,
        help = false
)
object HelpCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        if (context.hasArgs()) {
            val wrapper: CommandWrapper? = SnowyBot.COMMAND_HANDLER.commands[context.args[0]]
            if (wrapper == null) {
                context.reply("**Invalid command**: " + context.args[0])
                return
            }
            val help = StringBuilder("**Description**: ")
                    .append("${wrapper.description})")
                    .append("\n")
                    .append("**Usage**: !${wrapper.name} ${wrapper.parameters}")
                    .append("\n")
                    .append("**Category**: ${wrapper.category.id}")
                    .append("\n")
            if (wrapper.aliases.isNotEmpty())
                help.append("**Aliases**: ${java.lang.String.join(", ", wrapper.aliases)}")


            val embed: EmbedBuilder = EmbedBuilder()
                    .setAuthor("Snowy Assistant", null, context.event.jda.selfUser.avatarUrl)
                    .setColor(Color(255, 255, 254))
                    .addField("!".plus(wrapper.name), help.toString(), false)
                    .setFooter("Run !help for a list of all bot commands", context.event.guild.iconUrl)
            context.reply(embed)
            return
        }
        val b: Paginator.Builder = Paginator.Builder()
                .setItemsPerPage(5) //.setColumns(3)
                .setTimeout(1, TimeUnit.MINUTES)
                .setEventWaiter(CommandHandler.EVENT_WAITER)
                .setText(null as BiFunction<Int?, Int?, String?>?)
                .waitOnSinglePage(true)
                .setColor(Color.CYAN)
        val commands: MutableList<CommandWrapper> = ArrayList(SnowyBot.COMMAND_HANDLER.commands.values)
        commands.sortBy { command: CommandWrapper -> command.name }
        for (command in commands) {
            if (!command.help) continue
            b.addField("!" + command.name, """
                     **Description**: ${command.description}
            **Usage**: !${command.name} ${command.parameters}
             """.trimIndent(), false)
        }
        b.build().display(context.event.channel)

    }
}