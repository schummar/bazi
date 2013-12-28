@echo off
echo cloning ...
call git clone -q .. tmp
cd tmp
for /f %%f in ('git tag -l') do if not exist ..\%%f (
	echo build %%f ...
	call git checkout -q %%f
	call gradle --daemon -q fatJar
	mkdir ..\%%f
	if exist build\libs\bazi.jar (
		echo copy %%f artifacts ...
		copy build\libs\bazi.jar ..\%%f\bazi.jar >NUL
	)
)
cd ..
echo cleaning up ...
rmdir /s /q tmp
echo done.