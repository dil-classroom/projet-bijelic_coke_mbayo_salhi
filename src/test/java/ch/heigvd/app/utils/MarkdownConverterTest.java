package ch.heigvd.app.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MarkdownConverterTest class
 */
public class MarkdownConverterTest {

   @Test
   public void parserShouldParseImagesCorrectly() {
      String result = "<p><img src=\"./image.png\" alt=\"Une image\" /></p>\n";
      String input = "![Une image](./image.png)";
      String output = MarkdownConverter.convert(input);

      assertEquals(result,output);
   }

   @Test
   public void parserShouldParseLinksCorrectly() {
      String result = "<p><a href=\"www.google.com\">Lien vers " +
              "google</a></p>\n";
      String input = "[Lien vers google](www.google.com)";
      String output = MarkdownConverter.convert(input);

      assertEquals(result,output);
   }

   @Test
   public void parserShouldParseHeadingsCorrectly() {
      String result1 = "<h1>Mon premier article</h1>\n";
      String input1 = "# Mon premier article";
      String output1 = MarkdownConverter.convert(input1);

      assertEquals(result1,output1);

      String result2 = "<h2>Mon sous-titre</h2>\n";
      String input2 = "## Mon sous-titre";
      String output2 = MarkdownConverter.convert(input2);

      assertEquals(result2,output2);
   }

   @Test
   public void parserShouldParseParagraphsCorrectly() {
      String result = "<p>Le contenu de mon article.</p>\n";
      String input = "Le contenu de mon article.";
      String output = MarkdownConverter.convert(input);

      assertEquals(result,output);
   }


   @Test
   public void parserShouldParseWholePageCorrectly() {

      String result = "<h1>Mon premier article</h1>\n" +
              "<h2>Mon sous-titre</h2>\n" +
              "<p>Le contenu de mon article.\n" +
              "<img src=\"./image.png\" alt=\"Une image\" /></p>\n";

      String input = "# Mon premier article\n" +
              "## Mon sous-titre\n" +
              "Le contenu de mon article.\n" +
              "![Une image](./image.png)";

      String output = MarkdownConverter.convert(input);

      assertEquals(result, output);
   }




}
