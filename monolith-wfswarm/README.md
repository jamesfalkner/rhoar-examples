# CoolStore Monolith

This repository has the complete coolstore monolith built as a Java EE 7 application. To deploy it on OpenShift Container Platform (OCP) follow the instructions below



Build the project using openshift profile (use --offine if you downloaded the dependencies)

    mvn -Popenshift package

Create a new project (or use an existing)

    oc new-project coolstore

Create the app

    oc process -f src/main/openshift/template.json | oc create -f -

Start the build

    oc start-build coolstore --from-file=deployments/ROOT.war
    
    
To deploy the production environment and Jenkins pipeline

    oc process -f src/main/openshift/template-prod.json | oc create -f -
    
Manually start the pipeline

    oc start-build monolith-pipeline



 