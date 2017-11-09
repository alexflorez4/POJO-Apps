import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.System.exit;

public class AppZip
{
    private static Set<String> imagesToZipSet = new HashSet<>();
    private static Set<String> imagesZippedSet = new HashSet<>();

    public static void main(String[] args) throws IOException
    {
        //exec. java AppZip
        Scanner scanner = new Scanner(System.in);
        Set<String> imagesPathSet = new HashSet<>();

        File leadFile = new File("C:\\Users\\alexander-florez\\Documents\\TrainProjects\\VirtualPairProgrammers\\out\\production\\ZIPFiles\\files.txt");
        File remainingListings = new File("C:\\Users\\alexander-florez\\Documents\\TrainProjects\\VirtualPairProgrammers\\out\\production\\ZIPFiles\\BackListImages.txt");

        if(remainingListings.exists())
        {
            boolean goodChoice = true;
            do
            {
                System.out.println("Use existing file with remaining images? (y/n)");
                String useExistingFile = scanner.nextLine().toLowerCase();

                switch (useExistingFile)
                {
                    case "y":
                        imagesPathSet = getImagePath(remainingListings);
                        System.out.println("Using existing");
                        break;
                    case "n":
                        imagesPathSet = getImagePath(leadFile);
                        System.out.println("Starting over");
                        break;
                    default:
                        System.out.println("Invalid choice");
                        goodChoice = false;
                }
            }while (!goodChoice);
        }
        else
        {
            System.out.println("Enter File path (i.e. C:\\files.txt): ");
            String StartFile = scanner.nextLine();
            imagesPathSet = getImagePath(new File(StartFile));
        }


        int totalFilesFound = imagesPathSet.size();
        int numberFilesPerZipFile = 20;


        System.out.println(totalFilesFound + " Files found.");
        //scanner: number of files per zip file.

        for(String imagePath : imagesPathSet)
        {
                if (imagesToZipSet.size() < numberFilesPerZipFile)
                {
                    imagesToZipSet.add(imagePath);
                    if(imagesToZipSet.size() == numberFilesPerZipFile)
                    {
                        zipFiles(imagesToZipSet);
                        updateRemainingFilePaths(imagesPathSet, imagesToZipSet);
                    }
                }
        }
        if(imagesToZipSet.size()>0)
        {
            zipFiles(imagesToZipSet);
            updateRemainingFilePaths(imagesPathSet, imagesToZipSet);
        }
    }

    private static Set<String> getImagePath(File imageLocation) throws FileNotFoundException
    {
        Scanner sc = new Scanner(imageLocation);
        Set<String> images = new HashSet<>();

        while (sc.hasNext())
        {
            images.add(sc.nextLine());
        }
        return images;
    }

    private static void zipFiles(Set<String> images) throws IOException
    {
        FileOutputStream fos = null;

        try
        {
           fos  = new FileOutputStream("C:\\Users\\alexander-florez\\Documents\\TrainProjects\\VirtualPairProgrammers\\out\\production\\ZIPFiles\\imageMigration.zip");
        }
        catch (FileNotFoundException e)
        {
            e.getMessage();
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(fos);

        for(String srcFile : images)
        {
            File fileToZip = new File(srcFile);
            FileInputStream fileInputStream = null;
            try
            {
                fileInputStream = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOutputStream.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fileInputStream.read(bytes)) >= 0 )
                {
                    zipOutputStream.write(bytes, 0, length);
                }
                fileInputStream.close();
            }
            catch (FileNotFoundException e)
            {
                System.out.println(srcFile + " not found.  Appending to file.");
                filesNotFound(srcFile);
            }
        }
        zipOutputStream.close();
        fos.close();
    }

    private static void updateRemainingFilePaths(Set<String> imagesPathSet, Set<String> lastZippedImages)
    {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try
        {
            fw = new FileWriter("C:\\Users\\alexander-florez\\Documents\\TrainProjects\\VirtualPairProgrammers\\out\\production\\ZIPFiles\\BackListImages.txt");
            bw = new BufferedWriter(fw);

            for(String nextImage : imagesPathSet)
            {
                if(!lastZippedImages.contains(nextImage) && !imagesZippedSet.contains(nextImage))
                {
                    bw.write(nextImage + "\n");
                }
                else
                {
                    imagesZippedSet.add(nextImage);
                }
            }
            imagesToZipSet.clear();
            System.out.println("BackListImages File updated.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();

                Scanner scanner = new Scanner(System.in);
                boolean goodChoice = true;

                do
                {
                    System.out.println("Continue with next zip process: (y/n)");
                    String nextProcess = scanner.nextLine().toLowerCase();
                    switch (nextProcess){
                        case "y":
                            break;
                        case "n":
                            exit(0);
                        default:
                            System.out.println("Invalid choice");
                            goodChoice = false;
                    }
                }while (!goodChoice);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private static void filesNotFound(String srcFile)
    {

        final String FILE_NAME = "C:\\Users\\alexander-florez\\Documents\\TrainProjects\\VirtualPairProgrammers\\out\\production\\ZIPFiles\\skuNotFound.txt";

        BufferedWriter bw = null;
        FileWriter fw = null;

        File file = new File(FILE_NAME);

        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(srcFile + "\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

    }
}
