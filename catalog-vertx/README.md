# Intro

This is a Vert.x microservice that represents catalog information in a retail store. It uses the Fabric8 Maven Plugin to deploy to OpenShift.

Calls to the database are wrapped in a circuit breaker, providing fast fail in case of issues with the database.

The configuration of the application is provided by a Openshift ConfigMap. 

# Running on OpenShift

You'll need to have access to an OpenShift cluster and the `oc` command line interface.

1. Login to OpenShift and create a new project

```
oc login
oc new-project demo
```
2. Create a ConfigMap from the `app-config.yaml` file in the `etc` folder:

```
oc create configmap app-config --from-file=etc/app-config.yaml
```
3. Add the view role to the default service account. The application uses the Kubernetes API to retrieve the ConfigMap, which requires view access.

```
oc policy add-role-to-user view -z default
```

4. Deploy the MongoDB database using the template in the `ocp` folder

```
oc process -f ocp/coolstore-catalog-mongodb-persistent.yaml -p CATALOG_DB_USERNAME=mongo -p CATALOG_DB_PASSWORD=mongo  | oc create -f -
```

5. Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine & start the pod.

```
mvn clean fabric8:deploy -Popenshift  -DskipTests
```

This will build and deploy the microservice.

6. To test the service using curl:

To retrieve an individual product by id:

```
curl http://catalog-<project>.<domain>/product/444435
```
For example: 

```
% curl http://catalog-lab5.apps.127.0.0.1.nip.io/product/4444435
{
  "itemId" : "444435",
  "name" : "Oculus Rift",
  "desc" : "The world of gaming has also undergone some very unique and compelling tech advances in recent years. Virtual reality, the concept of complete immersion into a digital universe through a special headset, has been the white whale of gaming and digital technology ever since Nintendo marketed its Virtual Boy gaming system in 1995.",
  "price" : 106.0
}
```

To retrieve all the products in the catalog:

```
curl http://catalog-<project>.<domain>/products
```
For example:

```
% curl http://catalog-lab5.apps.127.0.0.1.nip.io/products
[ {
  "itemId" : "329299",
  "name" : "Red Fedora",
  "desc" : "Official Red Hat Fedora",
  "price" : 34.99
}, {
  "itemId" : "329299",
  "name" : "Forge Laptop Sticker",
  "desc" : "JBoss Community Forge Project Sticker",
  "price" : 8.5
}, {
  "itemId" : "165613",
  "name" : "Solid Performance Polo",
  "desc" : "Moisture-wicking, antimicrobial 100% polyester design wicks for life of garment. No-curl, rib-knit collar; special collar band maintains crisp fold; three-button placket with dyed-to-match buttons; hemmed sleeves; even bottom with side vents; Import. Embroidery. Red Pepper.",
  "price" : 17.8
}, {
  "itemId" : "165614",
  "name" : "Ogio Caliber Polo",
  "desc" : "Moisture-wicking 100% polyester. Rib-knit collar and cuffs; Ogio jacquard tape inside neck; bar-tacked three-button placket with Ogio dyed-to-match buttons; side vents; tagless; Ogio badge on left sleeve. Import. Embroidery. Black.",
  "price" : 28.75
}, {
  "itemId" : "165954",
  "name" : "16 oz. Vortex Tumbler",
  "desc" : "Double-wall insulated, BPA-free, acrylic cup. Push-on lid with thumb-slide closure; for hot and cold beverages. Holds 16 oz. Hand wash only. Imprint. Clear.",
  "price" : 6.0
}, {
  "itemId" : "444434",
  "name" : "Pebble Smart Watch",
  "desc" : "Smart glasses and smart watches are perhaps two of the most exciting developments in recent years. ",
  "price" : 24.0
}, {
  "itemId" : "444435",
  "name" : "Oculus Rift",
  "desc" : "The world of gaming has also undergone some very unique and compelling tech advances in recent years. Virtual reality, the concept of complete immersion into a digital universe through a special headset, has been the white whale of gaming and digital technology ever since Nintendo marketed its Virtual Boy gaming system in 1995.",
  "price" : 106.0
}, {
  "itemId" : "444436",
  "name" : "Lytro Camera",
  "desc" : "Consumers who want to up their photography game are looking at newfangled cameras like the Lytro Field camera, designed to take photos with infinite focus, so you can decide later exactly where you want the focus of each image to be.",
  "price" : 44.3
} ]
```
