@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\jena-arq-2.9.0-incubating.jar;"%REPO%"\commons-codec-1.5.jar;"%REPO%"\httpclient-4.1.2.jar;"%REPO%"\httpcore-4.1.3.jar;"%REPO%"\jena-arq-2.9.0-incubating-sources.jar;"%REPO%"\jena-arq-2.9.0-incubating-javadoc.jar;"%REPO%"\jena-core-2.7.0-incubating.jar;"%REPO%"\jena-core-2.7.0-incubating-sources.jar;"%REPO%"\jena-core-2.7.0-incubating-javadoc.jar;"%REPO%"\jena-iri-0.9.0-incubating.jar;"%REPO%"\jena-iri-0.9.0-incubating-sources.jar;"%REPO%"\icu4j-3.4.4.jar;"%REPO%"\xercesImpl-2.10.0.jar;"%REPO%"\xml-apis-1.4.01.jar;"%REPO%"\slf4j-api-1.6.4.jar;"%REPO%"\slf4j-log4j12-1.6.4.jar;"%REPO%"\log4j-1.2.16.jar;"%REPO%"\semanticwebsapere-pellet-2.3.0.jar;"%REPO%"\aterm-java-1.6-2.3.0.jar;"%REPO%"\pellet-core-2.3.0.jar;"%REPO%"\pellet-datatypes-2.3.0.jar;"%REPO%"\pellet-el-2.3.0.jar;"%REPO%"\pellet-rules-2.3.0.jar;"%REPO%"\pellet-jena-2.3.0.jar;"%REPO%"\pellet-explanation-2.3.0.jar;"%REPO%"\pellet-modularity-2.3.0.jar;"%REPO%"\pellet-pellint-2.3.0.jar;"%REPO%"\jgrapht-jdk1.5-2.3.0.jar;"%REPO%"\jetty-2.3.0.jar;"%REPO%"\jaxb-2.3.0.jar;"%REPO%"\antlr-2.3.0.jar;"%REPO%"\semanticwebsapere-experimenters-0.1.0.jar
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="queries-experimenter" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" it.apice.sapere.VisualQueriesTester %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%