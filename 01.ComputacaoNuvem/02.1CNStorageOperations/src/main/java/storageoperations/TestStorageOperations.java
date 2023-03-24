package storageoperations;

import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class TestStorageOperations {

    static int Menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Google Storage Operations:");
            System.out.println(" 0: List Buckets in Project");
            System.out.println(" 1: Create a new Bucket");
            System.out.println(" 2: Upload BLOB to a Bucket");
            System.out.println(" 3: Change Blob permission to: Public/Reader");
            System.out.println(" 4: Download Blob from a Bucket");
            System.out.println(" 5: Change Metadata of a Bucket Blob");
            System.out.println(" 6: Delete Blob");
            System.out.println(" 7: Delete All Blobs of a Bucket");
            System.out.println(" 8: Delete All Blobs as Batch");
            System.out.println(" 9: Delete a Bucket");
            //System.out.println(" 10: Set Write Access");
            System.out.println("99: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 10) || option == 99));
        return option;
    }

    static String getNameFor(String thing) {
        System.out.print("Introduce a " + thing + " > ");
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    static StorageClass getStorageClass() {
        Scanner scan = new Scanner(System.in);
        StorageClass[] CLASSES = new StorageClass[]{
                StorageClass.STANDARD,
                StorageClass.NEARLINE,
                StorageClass.COLDLINE,
                StorageClass.ARCHIVE
                //StorageClass.MULTI_REGIONAL,
                //StorageClass.DURABLE_REDUCED_AVAILABILITY
                //StorageClass.REGIONAL,
                };
        int option;
        do {
            System.out.println("Options for Google Storage Class:");
            System.out.println("1: STANDARD");
            System.out.println("2: NEARLINE");
            System.out.println("3: COLDLINE");
            System.out.println("4: ARCHIVE");
            System.out.print("Enter option number: ");
            option = scan.nextInt();
        } while (!(option > 0 && option <= 4));
        return CLASSES[option - 1];
    }

    static String getLocation() {
        Scanner scan = new Scanner(System.in);
        String[] LOCALS = new String[]{
                "northamerica-northeast1", "us-central1", "us-east1", "us-east4", "us-west1", "us-west2",
                "southamerica-east1",
                "europe-north1", "europe-west1", "europe-west2", "europe-west3", "europe-west4", "europe-west6",
                "asia-east1", "asia-east2", "asia-northeast1", "asia-south1", "asia-southeast1",
                "australia-southeast1", "asia", "eu", "us", "EUR4","NAM4"
        };
        int option;
        do {
            System.out.println("Region names:");
            System.out.println("North America:");
            System.out.println("1:northamerica-northeast1	 (Montréal)");
            System.out.println("2:us-central1	(Iowa)");
            System.out.println("3:us-east1	(South Carolina)");
            System.out.println("4: us-east4	(Northern Virginia)");
            System.out.println("5:us-west1	(Oregon)");
            System.out.println("6:us-west2	(Los Angeles)");
            System.out.println("South America:");
            System.out.println("7:southamerica-east1	(São Paulo)");
            System.out.println("Europe");
            System.out.println("8:europe-north1	(Finland)");
            System.out.println("9:europe-west1	(Belgium)");
            System.out.println("10:europe-west2	(London)");
            System.out.println("11:europe-west3	(Frankfurt)");
            System.out.println("12:europe-west4	(Netherlands)");
            System.out.println("13:europe-west6	(Zürich)");
            System.out.println("Asia:");
            System.out.println("14:asia-east1	(Taiwan)");
            System.out.println("15:asia-east2	(Hong Kong)");
            System.out.println("16:asia-northeast1	(Tokyo)");
            System.out.println("17:asia-south1	(Mumbai)");
            System.out.println("18:asia-southeast1	(Singapore)");
            System.out.println("Australia:");
            System.out.println("19:australia-southeast1	(Sydney)");
            System.out.println("Multi-regional locations:");
            System.out.println("20:asia	(Data centers in Asia)");
            System.out.println("21:eu	 (Data centers in the European Union1)");
            System.out.println("22:us	 (Data centers in the United States)");
            System.out.println("Dual-region locations:");
            System.out.println("23: EUR4 	EUROPE-NORTH1 and EUROPE-WEST4");
            System.out.println("24: NAM4 	US-CENTRAL1 and US-EAST1");
            System.out.print("Enter option number: ");
            option = scan.nextInt();
        } while (!(option > 0 && option <= 24));
        return LOCALS[option - 1];
    }

    static Bucket currentBucket = null;

    public static void main(String[] args) {
        //args[0] -{int, extvar, extfile
        // case extfile args[1] - abslutepath do key.json

        // Assumes the environment variable
        // set GOOGLE_APPLICATION_CREDENTIALS= < ServiceAccountroject.json>
        //Storage storage = StorageOptions.getDefaultInstance().getService();
        Storage storage = null;
        GoogleCredentials credentials = null;
        StorageOptions storageOptions=null;
        try {
            if (args.length == 0) {
                storageOptions=StorageOptions.getDefaultInstance();
                storage = storageOptions.getService();
            } else if (args.length == 1) {
                if (args[0].equals("int")) {
                    credentials = ComputeEngineCredentials.create();
                    storageOptions = StorageOptions
                            .newBuilder()
                            .setCredentials(credentials)
                            .build();
                    storage = storageOptions.getService();
                } else if (args[0].equals("extvar")) {
                    storageOptions=StorageOptions.getDefaultInstance();
                    storage = storageOptions.getService();
                } else Usage();
            } else if (args.length == 2 && args[0].equals("extfile")) { // extfile >file key.json
                credentials = GoogleCredentials.fromStream(new FileInputStream(args[1]));
                 storageOptions = StorageOptions
                        .newBuilder()
                        .setCredentials(credentials)
                        .build();
                storage = storageOptions.getService();
            }

            String projID = storageOptions.getProjectId();

            if (projID != null)
                System.out.println("Current Project ID:" + projID);
            else {
                System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
                System.exit(-1);
            }
            StorageOperations soper = new StorageOperations(storage);
            boolean end = false;
            while (!end) {
                try {
                    int option = Menu();

                    switch (option) {
                        case 0:
                            soper.listBuckets(projID);
                            break;
                        case 1:
                            currentBucket = soper.CreateBucket(getNameFor("Bucket"), getStorageClass(), getLocation());
                            break;
                        case 2:
                            BlobId currentBlob = soper.addBlobToBucket();
                            break;
                        case 3:
                            soper.changeBlobAccessPublicReader();
                            break;
                        case 4:
                            soper.downloadBlob();
                            break;
                        case 5:
                            soper.changeBlobMetadata();
                            break;
                        case 6:
                            soper.deleteBlob();
                            break;
                        case 7:
                            soper.deleteAllBlobsOfBucket();
                            break;
                        case 8:
                            soper.batchDeleteAllBlobs();
                            break;
                        case 9:
                            soper.deleteBucket();
                            break;
                        case 10:
                            soper.changeBlobToWriteAccess();
                            break;
                        case 99:
                            System.exit(0);
                    }
                } catch (Exception ex ) {
                    System.out.println("Error executing operations!");
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error!...:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void Usage() {
        System.out.println("Use: TesteStorageOperations [ int | extvar | extfile <key.json file>  ] // default extvar");
        System.exit(-1);
    }

}
