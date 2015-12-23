package io.gavinhungry.xposed.ambientnotifications;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodReplacement;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class AmbientNotifications implements IXposedHookLoadPackage, IXposedHookInitPackageResources {

  @Override
  public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    if (!lpparam.packageName.equals("com.android.systemui")) {
      return;
    }

    XposedHelpers.findAndHookMethod("com.android.systemui.doze.DozeService", lpparam.classLoader,
      "listenForPulseSignals", boolean.class, new XC_MethodReplacement() {

      @Override
      protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        Boolean listen = (Boolean) param.args[0];
        return XposedHelpers.callMethod(param.thisObject, "listenForNotifications", listen);
      }
    });
  }

  @Override
  public void handleInitPackageResources(final InitPackageResourcesParam ipparam) throws Throwable {
    if (!ipparam.packageName.equals("com.android.systemui")) {
      return;
    }

    ipparam.res.setReplacement("com.android.systemui", "integer", "doze_pulse_duration_visible", 6000);
  }
}
