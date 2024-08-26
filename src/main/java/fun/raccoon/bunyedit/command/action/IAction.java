package fun.raccoon.bunyedit.command.action;

import java.util.List;

import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandSender;

public interface IAction {
    public boolean apply(I18n i18n, CommandSender sender, List<String> argv);
}