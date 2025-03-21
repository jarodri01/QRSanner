import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class Scanner {

        /**
         * Reads a QR code from an image file and returns the text content.
         *
         * @param filePath Path to the QR code image file.
         * @return Decoded text as String.
         */
        public String readQRCode(String filePath) {
            try {
                // Load the image file
                BufferedImage bufferedImage = ImageIO.read(new File(filePath));

                // Convert the image to a binary bitmap source for decoding
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Result result = new MultiFormatReader().decode(bitmap);

                // Return the decoded text
                return result.getText();
            } catch (NotFoundException e) {
                return "QR Code not found in the image.";
            } catch (IOException e) {
                return "Error reading the image file.";
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner();
            String filePath = "path/to/your/qr-code-image.png"; // Replace with your QR code image path
            String result = scanner.readQRCode(filePath);

            System.out.println("Decoded QR Code text: " + result);
        }
    }

