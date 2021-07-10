; For unicode file should be in UTF8 format WITH BOM
Unicode true

!include "MUI2.nsh"
!include "FileFunc.nsh"
!insertmacro RefreshShellIcons
!insertmacro un.RefreshShellIcons

;--------------------------------

!define HOME "E:\MaknoonIslamicEncyclopedia"

!define MUI_ICON "${HOME}\images.nsis\icon_setup.ico"
!define MUI_UNICON "${HOME}\images.nsis\uninstall.ico"

!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${HOME}\images.nsis\install_en.bmp"
!define MUI_HEADERIMAGE_BITMAP_RTL "${HOME}\images.nsis\install_ar.bmp"
!define MUI_HEADERIMAGE_UNBITMAP "${HOME}\images.nsis\install_en.bmp"
!define MUI_HEADERIMAGE_UNBITMAP_RTL "${HOME}\images.nsis\install_ar.bmp"
!define MUI_WELCOMEFINISHPAGE_BITMAP "${HOME}\images.nsis\welcome.bmp"

!define PROGRAM_NAME_AR "المواريث والزكاة"
!define MAKNOON_APPS "برامج مكنون"
!define PROGRAM_NAME "Maknoon Islamic Encyclopedia"

;--------------------------------

OutFile "MaknoonIslamicEncyclopediaVMx64.exe"

RequestExecutionLevel admin			; To avoid shortcut removal problem [http://nsis.sourceforge.net/Shortcuts_removal_fails_on_Windows_Vista]

; The default installation directory
InstallDir "$PROGRAMFILES64\${PROGRAM_NAME}"

; Registry key to check for directory (so if you install again, it will overwrite the old one automatically)
InstallDirRegKey HKLM "SOFTWARE\${PROGRAM_NAME}" "Install_Dir"

;--------------------------------
;Pages

!define MUI_WELCOMEPAGE_TITLE "$(WELCOME_TITLE)"
!define MUI_WELCOMEPAGE_TEXT "$(WELCOME_TEXT)"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

!insertmacro MUI_LANGUAGE "Arabic"
!insertmacro MUI_LANGUAGE "English"

LangString WELCOME_TEXT ${LANG_ENGLISH} "AlSalam Alikom$\r$\n\
$\r$\n\
The program consists of:$\r$\n\
1. Mawareth: Calculating inheritance under Islamic rules.$\r$\n\
2. Zakat: Calculating Zakat under Islamic rules$\r$\n\
$\r$\n\
This product is licensed to all Muslims$\r$\n\
Copyright©2021 Maknoon.com$\r$\n\
This product is not for sale$\r$\n\
Version 2.8"
LangString WELCOME_TEXT ${LANG_ARABIC} "السلام عليكم ورحمة الله وبركاته$\r$\n\
$\r$\n\
يتضمن البرنامج:$\r$\n\
1. المواريث لحساب الفرائض: يقوم بحساب التركات وتصحيح المسائل وإجراء المناسخات والعول والرد والحجب وتوريث الخنثى المشكل والمفقود والحمل والعبد المبعض وذوي الأرحام حسب المذاهب الأربعة.$\r$\n\
2. الزكاة لحساب الزكوات: يقوم بعمل جيد في حساب زكاة الأنعام وزكاة الذهب إذ فيه إمكانية تحديد قيمة الذهب والفضة.$\r$\n\
$\r$\n\
البرنامج مصرح لنشره واستخدامه من جميع المسلمين$\r$\n\
جميع الحقوق محفوظة لموقع مكنون$\r$\n\
يمنع بيع البرنامج$\r$\n\
الإصدار 2.8"

LangString WELCOME_TITLE ${LANG_ENGLISH} "${PROGRAM_NAME}"
LangString WELCOME_TITLE ${LANG_ARABIC} "${PROGRAM_NAME_AR}"

LangString PUBLISHER_NAME ${LANG_ENGLISH} "Maknoon Apps"
LangString PUBLISHER_NAME ${LANG_ARABIC} "${MAKNOON_APPS}"

LangString SHORTCUT_START ${LANG_ENGLISH} "Mawareth & Zakat"
LangString SHORTCUT_START ${LANG_ARABIC} "${PROGRAM_NAME_AR}"

LangString INSTALLER_NAME ${LANG_ENGLISH} "${PROGRAM_NAME}"
LangString INSTALLER_NAME ${LANG_ARABIC} "${PROGRAM_NAME_AR}"

Function .onInit

	ReadRegStr $R0 HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "UninstallString"
	StrCmp $R0 "" cont
	
	ReadRegStr $LANGUAGE HKLM "SOFTWARE\${PROGRAM_NAME}" Lang
	; Not working because language is initialized after .onInit for LangString
	;MessageBox MB_YESNOCANCEL|MB_ICONINFORMATION $(UNINSTALL_MESSAGE) IDYES uninst IDCANCEL abort
	;Goto done
	StrCmp $LANGUAGE "1033" true false
	true:
		MessageBox MB_YESNOCANCEL|MB_ICONINFORMATION "${PROGRAM_NAME} is already installed. Do you want to remove the previous version?" IDYES uninst IDCANCEL abort
		Goto done
	false:
		MessageBox MB_YESNOCANCEL|MB_ICONINFORMATION "هناك نسخة أخرى من برنامج المواريث والزكاة على جهازك. هل تريد إزالتها؟" IDYES uninst IDCANCEL abort
		Goto done
	
	;Run the uninstaller
	uninst:
		ClearErrors
		ExecWait '$R0 _?=$INSTDIR' ;Do not copy the uninstaller to a temp file
		
		IfErrors no_remove_uninstaller
			;You can either use Delete /REBOOTOK in the uninstaller or add some code
			;here to remove the uninstaller. Use a registry key to check
			;whether the user has chosen to uninstall. If you are using an uninstaller
			;components page, make sure all sections are uninstalled.
		no_remove_uninstaller:
		Goto done
	
	abort:
		Abort
	
	cont:
	
	;Default is Arabic
	WriteRegStr HKLM "SOFTWARE\${PROGRAM_NAME}" "Lang" "1025"
	!define MUI_LANGDLL_REGISTRY_ROOT HKLM
	!define MUI_LANGDLL_REGISTRY_KEY "Software\${PROGRAM_NAME}"
	!define MUI_LANGDLL_REGISTRY_VALUENAME "Lang"
	!define MUI_LANGDLL_ALWAYSSHOW
	
	;Language selection dialog
	;we will not use this since Arabic word is not displayed in Arabic. It is being solved by Unicode NSIS.
	!define MUI_LANGDLL_WINDOWTITLE "Program Language لغة البرنامج"
	!define MUI_LANGDLL_INFO "Select the language اختر لغة البرنامج"
	!insertmacro MUI_LANGDLL_DISPLAY
	;Push ""
	;Push ${LANG_ARABIC}
	;Push العربية
	;Push ${LANG_ENGLISH}
	;Push English
	;Push A	; A means auto count languages
			; for the auto count to work the first empty push (Push "") must remain
	;LangDLL::LangDialog "Program Language لغة البرنامج" "Select the language اختر لغة البرنامج"

	Pop $LANGUAGE
	StrCmp $LANGUAGE "cancel" 0 +2
		Abort
	done:

FunctionEnd

; The name of the installer
Name "$(INSTALLER_NAME)"
BrandingText "$(INSTALLER_NAME)"
VIProductVersion "2.8.0.0"
VIAddVersionKey "ProductName" "${PROGRAM_NAME_AR}"
VIAddVersionKey "CompanyName" "${MAKNOON_APPS}"
VIAddVersionKey "LegalCopyright" "©maknoon.com"
VIAddVersionKey "FileDescription" "${PROGRAM_NAME_AR}"
VIAddVersionKey "FileVersion" "2.8"
VIAddVersionKey "InternalName" "${PROGRAM_NAME}"

; The stuff to install
Section "${PROGRAM_NAME}" SEC_IDX

	SectionIn RO			
	
	SetOutPath "$INSTDIR\lib"		; Set output path to the installation directory.
	File /r lib\*.*					; Put file there

	SetOutPath "$INSTDIR\jdk"
	File /r jdk\*.*

	SetOutPath "$INSTDIR\com"
	File /r mods\*.*

	SetOutPath "$INSTDIR\language"
	File /r language\*.*
	
	SetOutPath "$INSTDIR"
	File "icon.ico"
	File "startup.bat"

	; Write the installation path & the language into the registry
	WriteRegStr HKLM "SOFTWARE\${PROGRAM_NAME}" "Install_Dir" "$INSTDIR"
	WriteRegStr HKLM "SOFTWARE\${PROGRAM_NAME}" "Lang" "$LANGUAGE"
	
	; Write the uninstall keys for Windows
	; http://nsis.sourceforge.net/Add_uninstall_information_to_Add/Remove_Programs
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "DisplayName" "$(INSTALLER_NAME)"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "URLInfoAbout" "https://www.maknoon.com"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "DisplayIcon" "$INSTDIR\icon.ico"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "DisplayVersion" "2.8"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "Publisher" "$(PUBLISHER_NAME)"
	WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "UninstallString" '"$INSTDIR\uninstall.exe"'
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "NoModify" 1
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "NoRepair" 1
	WriteUninstaller "$INSTDIR\uninstall.exe"
	
	; Slow
	;${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
	;IntFmt $0 "0x%08X" $0
	
	; http://nsis.sourceforge.net/Add_uninstall_information_to_Add/Remove_Programs
	SectionGetSize ${SEC_IDX} $0
	IntFmt $0 "0x%08X" $0
	WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}" "EstimatedSize" "$0"
	
	; To allow all users to svae setting in Vista/W7 because of UAC
	AccessControl::GrantOnFile "$INSTDIR" "(BU)" "FullAccess"
	
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

	CreateShortCut "$SMPROGRAMS\$(SHORTCUT_START).lnk" "$INSTDIR\jdk\bin\javaw" "-cp . --module-path mods;lib -m com.maknoon/com.maknoon.MaknoonIslamicEncyclopedia" "$INSTDIR\icon.ico"
	
	; Create desktop shortcut
	CreateShortCut "$DESKTOP\$(INSTALLER_NAME).lnk" "$INSTDIR\jdk\bin\javaw" "-cp . --module-path mods;lib -m com.maknoon/com.maknoon.MaknoonIslamicEncyclopedia" "$INSTDIR\icon.ico"

SectionEnd

Section "Uninstall"

	; Remove registry keys
	DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${PROGRAM_NAME}"
	DeleteRegKey HKLM "SOFTWARE\${PROGRAM_NAME}"

	; Remove files and uninstaller
	;Delete /REBOOTOK $INSTDIR\*.*
	
	; Remove shortcuts, if any
	;Delete /REBOOTOK $SMPROGRAMS\$(INSTALLER_NAME)\*.*

	; Remove shortcut used
	Delete "$DESKTOP\$(INSTALLER_NAME).lnk"
	Delete "$SMPROGRAMS\$(SHORTCUT_START).lnk"
	RMDir /r "$INSTDIR"

SectionEnd

;--------------------------------
;Uninstaller Functions

Function un.onInit
	ReadRegStr $LANGUAGE HKLM "SOFTWARE\${PROGRAM_NAME}" Lang
FunctionEnd