package xyz.roolurker.colorbot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created with love by rooLurk on 1/5/17.
 */

/**
 * Copyright (c) 2017 Markus "rooLurk" Isberg
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

class ColorBot(token: String) : ListenerAdapter() {
	val jda: JDABuilder = JDABuilder(AccountType.BOT)
			.addListener(this)
			.setToken(token)
	var bot: JDA? = null
	val commandChan: String = "266598610659966976"

	fun run() {
		bot = jda.buildBlocking()
	}

	override fun onMessageReceived(e: MessageReceivedEvent?) {
		super.onMessageReceived(e)
		if (e == null) {
			return
			//
		} else if (e.channel.id != commandChan || e.author.isBot || e.channel.type != ChannelType.TEXT) {
			return
		}
		val msg: String = e.message.rawContent
		val split: List<String> = msg.split(" ", limit = 2)
		if (split.isNotEmpty()) {
			if ("!color" == split[0] || "!colour" == split[0]) {
				e.channel.sendMessage(doRoleStuff(e.guild, e.member, split)).queue()
			}
		}
	}

	fun doRoleStuff(guild: Guild, member: Member, split: List<String>): String {
		if (split.size == 2) {
			val par2: String = split[1]
			if ("remove" == par2) {
				val oldRole: Role? = getColorRole(member)
				if (oldRole == null) {
					return "${member.effectiveName}, you do not have a color!"
				} else {
					guild.controller.removeRolesFromMember(member, oldRole).queue()
					return "${member.effectiveName}, done!"
				}
			}
			try {
				val i: Int = par2.toInt()
				val hex: String? = indexToHex(i)
				if (hex == null) {
					return "${member.effectiveName}, invalid index.\nUsage: `!color <index>`. See channel topic for available colors."
				} else {
					guild.roles.filter { hex == it.name && it.permissionsRaw == 0x00000000L }.forEach {
						return addRole(guild, hex, member)
					}
					return "${member.effectiveName}, role could not be found, this is an issue on the guild's end."
				}
			} catch (e: NumberFormatException) {
				return "${member.effectiveName}, invalid index.\nUsage: `!color <index>`. See channel topic for available colors."
			}
		} else {
			return "Usage: `!color <index>`. See channel topic for available colors."
		}
	}

	fun addRole(guild: Guild, hex: String, member: Member): String {
		val oldRole: Role? = getColorRole(member)
		if (oldRole != null) {
			return "${member.effectiveName}, you already have a color! Please remove it with `!color remove` first."
		}
		guild.roles.filter { it.name == hex }.forEach {
			guild.controller.addRolesToMember(member, it).queue()
			return "${member.effectiveName}, done! Enjoy your new color."
		}
		return "Unknown error"
	}

	fun getColorRole(member: Member): Role? {
		return member.roles.firstOrNull { it.name.startsWith("#") && it.name.length == 7 && it.permissionsRaw == 0x00000000L }
	}

	fun indexToHex(index: Int): String? {
		when (index) {
			1 -> return "#C1C1C1"
			2 -> return "#808080"
			3 -> return "#010101"
			4 -> return "#88CBE7"
			5 -> return "#82ADE2"
			6 -> return "#4169E1"
			7 -> return "#3059FF"
			8 -> return "#5F5FCD"
			9 -> return "#5E4DD6"
			10 -> return "#07025E"
			11 -> return "#90FFFF"
			12 -> return "#00FFFF"
			13 -> return "#40E0D0"
			14 -> return "#00FFCC"
			15 -> return "#00FA9A"
			16 -> return "#05C2C5"
			17 -> return "#008080"
			18 -> return "#3CB371"
			19 -> return "#1C9451"
			20 -> return "#3EBB2D"
			21 -> return "#00FF00"
			22 -> return "#338A01"
			23 -> return "#008000"
			24 -> return "#A5D769"
			25 -> return "#ADFF2F"
			26 -> return "#FAD7AE"
			27 -> return "#FFFF00"
			28 -> return "#FFDD00"
			29 -> return "#E6B907"
			30 -> return "#EB9A3B"
			31 -> return "#CF7A27"
			32 -> return "#FF9000"
			33 -> return "#C96016"
			34 -> return "#FF4500"
			35 -> return "#EC8F6A"
			36 -> return "#EC7171"
			37 -> return "#FF0000"
			38 -> return "#B00B1E"
			39 -> return "#DB133B"
			40 -> return "#FF0060"
			41 -> return "#FCAAB9"
			42 -> return "#FF69B4"
			43 -> return "#FF00FF"
			44 -> return "#FF1493"
			45 -> return "#D3138C"
			46 -> return "#A72982"
			47 -> return "#AC25B1"
			48 -> return "#8A2BE2"
			49 -> return "#BB55FF"
			50 -> return "#A869FF"
		}
		return null
	}
}
