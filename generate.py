import sys
import os
import shutil

mod_id = input("Mod ID: ")
mod_name = input("Mod name: ")
mod_class = input("Mod class: ")
mod_description = input("Mod description: ")

def expand_placeholders(path: str):
    print(f"Expanding placeholders in {path}... ", end="")
    with open(path, "r") as f:
        content = f.read()
    content = content.replace('${MOD_ID}', mod_id)
    content = content.replace('${MOD_NAME}', mod_name)
    content = content.replace('${MOD_CLASS}', mod_class)
    content = content.replace('${MOD_DESCRIPTION}', mod_description)
    with open(path, "w") as f:
        f.write(content)
    print("Done!")

os.makedirs(os.path.join("src/main/java/io/github/ashisbored", mod_id))
shutil.move('MainClass.java', os.path.join("src/main/java/io/github/ashisbored", mod_id, f'{mod_class}.java'))

expand_placeholders('build.gradle.kts')
expand_placeholders('settings.gradle.kts')
expand_placeholders('src/main/resources/fabric.mod.json')
expand_placeholders(f'src/main/java/io/github/ashisbored/{mod_id}/{mod_class}.java')

print("Setting up gradle wrapper...")
os.system('gradle --no-daemon wrapper')

input("Press enter to delete this script or ctrl-c to abort...")
os.remove(sys.argv[0])

print("Commiting template changes...")
os.system('git add .')
os.system('git commit -m "Expand template"')
