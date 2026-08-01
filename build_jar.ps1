# Full Compilation Script for ScopeCraft Minecraft 1.8.9 Forge Mod
$java8Home = "C:\Program Files\Java\jdk1.8.0_202"
$javac = "$java8Home\bin\javac.exe"
$jar = "$java8Home\bin\jar.exe"

$forgeBin = "C:\Users\Ulysse\.gradle\caches\minecraft\net\minecraftforge\forge\1.8.9-11.15.1.2318-1.8.9\stable\22\forgeBin-1.8.9-11.15.1.2318-1.8.9.jar"
$forgeUserDevClasses = "C:\Users\Ulysse\.gradle\caches\minecraft\net\minecraftforge\forge\1.8.9-11.15.1.2318-1.8.9\userdev\classes.jar"
$launchWrapper = "C:\Users\Ulysse\.gradle\caches\modules-2\files-2.1\net.minecraft\launchwrapper\1.12\111e7bea9c968cdb3d06ef4632bf7ff0824d0f36\launchwrapper-1.12.jar"
$asm = "C:\Users\Ulysse\.gradle\caches\modules-2\files-2.1\org.ow2.asm\asm-debug-all\5.0.3\f9e364ae2a66ce2a543012a4668856e84e5dab74\asm-debug-all-5.0.3.jar"
$gson = "C:\Users\Ulysse\AppData\Roaming\.minecraft\libraries\com\google\code\gson\gson\2.2.4\gson-2.2.4.jar"
$lwjgl = "C:\Users\Ulysse\AppData\Roaming\.minecraft\libraries\org\lwjgl\lwjgl\lwjgl\2.9.4-nightly-20150209\lwjgl-2.9.4-nightly-20150209.jar"

$classpath = "$forgeBin;$forgeUserDevClasses;$launchWrapper;$asm;$gson;$lwjgl"

Remove-Item -Recurse -Force build/bin -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path build/bin, build/libs -Force

$sources = Get-ChildItem -Path src/main/java -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

& $javac -encoding UTF-8 -cp $classpath -d build/bin $sources

if ($LASTEXITCODE -eq 0) {
    Copy-Item -Path src/main/resources/* -Destination build/bin -Recurse -Force
    & $jar cvf build/libs/ScopeCraft-1.0.0.jar -C build/bin .
    
    # Copy directly into active PrismLauncher mods directory
    Copy-Item -Path 'build/libs/ScopeCraft-1.0.0.jar' -Destination 'C:\Users\Ulysse\AppData\Roaming\PrismLauncher\instances\PVPack - PvP Pack for Hypixel(1)\minecraft\mods\ScopeCraft-1.0.0.jar' -Force
    Write-Host "`n[SUCCESS] ScopeCraft-1.0.0.jar compile et copie dans le dossier mods PrismLauncher !" -ForegroundColor Green
} else {
    Write-Host "`n[ERROR] Echec de la compilation Forge." -ForegroundColor Red
}
