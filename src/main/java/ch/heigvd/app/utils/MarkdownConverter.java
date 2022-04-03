package ch.heigvd.app.utils;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
/**
 * MarkdownConverter class
 */
public class MarkdownConverter {

   /**
    * Converts a markdown file to html
    * @param input input markdown text
    * @return input text converted to html
    * @see <a href="https://github.com/vsch/flexmark-java">FlexMark</a>
    */
   public static String convert(String input) {

      // Used to add options
      MutableDataSet options = new MutableDataSet();

      Parser parser = Parser.builder(options).build();
      HtmlRenderer renderer = HtmlRenderer.builder(options).build();
      Node document = parser.parse(input);

      return renderer.render(document);
   }
}
