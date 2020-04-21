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
package io.github.reflxction.snowy.command.admin

import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageHistory
import net.dv8tion.jda.api.exceptions.ContextException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@BotCommand(
        name = "delete",
        description = "Delete a certain amount of messages",
        category = CommandCategory.ADMIN,
        minimumArguments = 1,
        requiredPermissions = [Permission.MESSAGE_MANAGE],
        parameters = "<amount to delete>"
)
object BulkDeleteCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val c = context.event.channel
        val history = MessageHistory(c)
        val num = context.resolve(0, Int::class.java)
        if (num < 2 || num > 100) throw CommandCallback.CommandException("**Invalid amount**: $num. Must be greater than 2 and less than 100.")
        try {
            context.event.message.delete().queue()

            val messages = history.retrievePast(num).complete()
            context.event.channel.purgeMessages(messages).forEach { future ->
                future.thenApply { context.reply("Successfully deleted $num messages.", Consumer { m -> m.delete().queueAfter(5, TimeUnit.SECONDS) }) }
            }
        } catch (ignored: ContextException) { // messages are less than the specified amount. useless to handle z
        }
    }
}