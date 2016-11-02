#!/bin/bash


###################
# bash only
# usage: ./package.sh
#####################


#declare -a : define an array
#declare -i : define an integer
declare -i index=1
declare -a envMap
declare -a packageMap
declare -a formatMap=(["1"]="tar" ["2"]="tar.gz")

CURRENT_DIR=`dirname $0`
cd $CURRENT_DIR

#scan the packages from scritps/*
for i in scripts/*; do
  if [ -d ${i} ]; then
    packageMap["${index}"]="`basename ${i}`"
    index=index+1
  fi
done

#scan the envs from conf/*
index=1
for i in conf/*; do
  if [ -d ${i} ]; then
    envMap["${index}"]="`basename ${i}`"
    index=index+1
  fi
done


env=us_dev
package=kafka
format=2
#version=`date +%Y%m%d-%H%M%S`

echo "Please choose the environment [default ${env}]:"
for i in ${!envMap[@]}; do echo - ${envMap[${i}]}; done
read envTmp


echo ""
echo "Please choose the package [default ${package}]:"
for i in ${!packageMap[@]}; do echo - ${packageMap[${i}]}; done
read packageTmp

#echo ""
#echo "Please choose the package format by typing the number only [default ${format}]:"
#for i in ${!formatMap[@]}; do echo [${i}] : ${formatMap[${i}]}; done
#read formatTmp

#echo ""
#echo "Please type the package version (date format like 20150324-113434 is suggested) [default ${version}]:"
#read versionTmp


if [ "${envTmp}" != "" ]; then
  env=${envTmp}
fi
if [ "${packageTmp}" != "" ]; then
  package=${packageTmp}
fi

#if [ "${formatTmp}" != "" ]; then
#format=formatTmp
#fi

#if [ "${versionTmp}" != "" ]; then
#version=versionTmp
#fi


echo "[INFO] You are choosing environment: ${env}" 
echo "[INFO] You are choosing package: ${package}" 
echo "[INFO] You are choosing package format: ${formatMap[${format}]}" 
#echo "[INFO] You are choosing package version: ${version}" 

install_tamboo_modules () {
	cd ..
	echo "install tamboo modules"
	mvn clean install -Dmaven.test.skip=true
	if [ $? -ne 0 ]; then
	  echo "[ERROR] failed to install tamboo modules"
	  exit 1
	fi
	cd -
}
install_tamboo_modules


theCommand="mvn clean assembly:single -Dpackage.environment=${env} -Dpackage=${package} -Dpackage.format=${formatMap[${format}]}"
echo ${theCommand}
exec ${theCommand}
