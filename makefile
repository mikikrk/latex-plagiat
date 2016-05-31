defaultCommand = mvn clean install
installCommand = mvn install
skipTestsCommand = $(defaultCommand) -DskipTests
skipUnitTestsCommand = $(defaultCommand) -DskipUTs
skipIntegrationTestsCommand = $(defaultCommand) -DskipITs
cleanCommand = mvn clean

all :
	$(defaultCommand)
	
no_tests :
	$(skipTestsCommand)

no_unit_tests :
	$(skipUnitTestsCommand)
	
no_int_tests :
	$(skipIntegrationTestsCommand)	
	
clean :
	$(cleanCommand)