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
package io.github.reflxction.snowy.command.misc

import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCallback.CommandException
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color

@BotCommand(
        name = "color",
        description = "Generate an RGB color and get other information about it",
        parameters = "<rgb> OR <red> <green> <blue>",
        category = CommandCategory.USER,
        aliases = ["rgb"],
        minimumArguments = 1
)
object ColorCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        try {
            when (context.args.size) {
                1 -> {
                    val color = Color(context.resolve(0, Int::class.java))
                    context.reply(EmbedBuilder().setTitle("Preview color").setColor(color).setDescription(describe(color)))
                }
                3 -> {
                    val color = Color(check255(context.resolve(0, Int::class.java), "Red"), check255(context.resolve(1, Int::class.java), "Green"), check255(context.resolve(2, Int::class.java), "Blue"))
                    context.reply(EmbedBuilder().setTitle("Preview color").setColor(color).setDescription(describe(color)))
                }
                else -> context.invalidUsage()
            }
        } catch (e: Exception) {
            throw CommandException("Invalid color arguments")
        }
    }

    private fun describe(color: Color): String {
        return StringBuilder("**RGB**: ${color.rgb}\n\n")
                .append("**Red**: ${color.red}\n")
                .append("**Green**: ${color.green}\n")
                .append("**Blue**: ${color.blue}\n")
                .append("**Hex**: ${toHex(color)}")
                .toString()
    }

    private fun check255(color: Int, name: String): Int {
        if (color < 0 || color > 255)
            throw CommandException("$name is invalid: **$color**")
        return color
    }

    private fun toHex(color: Color): String {
        return "#" + hex(color.red) + hex(color.green) + hex(color.blue)
    }

    private fun hex(number: Int): String {
        val builder = StringBuilder(Integer.toHexString(number and 0xff))
        while (builder.length < 2) {
            builder.append("0")
        }
        return builder.toString().toUpperCase()
    }

}