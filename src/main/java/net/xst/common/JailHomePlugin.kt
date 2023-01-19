package net.xst.common

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.coroutines.annotations.installCoroutineSupport
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.minecraft.extras.MinecraftHelp
import cloud.commandframework.paper.PaperCommandManager
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.github.shynixn.mccoroutine.bukkit.scope
import com.google.inject.Guice
import com.google.inject.Injector
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.xst.common.injection.SpigotModule
import net.xst.common.util.SpigotExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.function.Function
import kotlin.reflect.KClass

//TODO: Move this to a separate project once we have an actual server and a maven repo
abstract class JailHomePlugin : SuspendingJavaPlugin() {
	private lateinit var injector: Injector

	lateinit var commandManager: PaperCommandManager<Player>
	lateinit var bukkitAudiences: BukkitAudiences
	lateinit var minecraftHelp: MinecraftHelp<Player>
	lateinit var annotationParser: AnnotationParser<Player>

	override suspend fun onEnableAsync() {
		injector = Guice.createInjector(SpigotModule(this))

		val toPlayerMapper = Function<CommandSender, Player> { it as Player }
		val fromPlayerMapper = Function<Player, CommandSender> { it }

		val coordinator =
			AsynchronousCommandExecutionCoordinator.newBuilder<Player>().withAsynchronousParsing().withExecutor(
				SpigotExecutor(this)
			).build()

		commandManager = PaperCommandManager(this, coordinator, toPlayerMapper, fromPlayerMapper)
		bukkitAudiences = BukkitAudiences.create(this)
		minecraftHelp =
			MinecraftHelp("/help template", { return@MinecraftHelp bukkitAudiences.sender(it) }, commandManager)
		commandManager.registerBrigadier()
		commandManager.registerAsynchronousCompletions()
		annotationParser = AnnotationParser(commandManager, Player::class.java) {
			return@AnnotationParser CommandMeta.simple()
				.with(CommandMeta.DESCRIPTION, it.get(StandardParameters.DESCRIPTION, "No description")).build()
		}

		annotationParser.installCoroutineSupport(this.scope)
	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}

	protected fun registerListeners(vararg listeners: KClass<out Listener>) {
		listeners.forEach {
			server.pluginManager.registerSuspendingEvents(injector.getInstance(it.java), this)
		}
	}

	protected fun registerCommands(vararg commands: KClass<out Any>) {
		commands.forEach {
			annotationParser.parse(injector.getInstance(it.java))
		}
	}
}