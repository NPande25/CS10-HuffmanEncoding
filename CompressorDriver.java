import java.io.IOException;
import java.util.Map;

/**
 * @author nikhilpande
 * @author nathanmcallister
 */
public class CompressorDriver {
    public static void main(String[] args) throws IOException {
        HuffmanCompress huff = new HuffmanCompress();

        // helloWorld example:
        // use substring method to create file names
        String hw = "inputs/helloWorld.txt";
        String hwCompressed = hw.substring(0, hw.length() - 4) + "_compressed.txt";
        String hwDecompressed = hw.substring(0, hw.length() - 4) + "_decompressed.txt";

        BinaryTree<CodeTreeElement> hwtree = huff.makeCodeTree(huff.countFrequencies(hw)); // make the code tree
        Map<Character, String> hwmap = huff.computeCodes(hwtree); // make the map with characters and codes
        huff.compressFile(hwmap, hw, hwCompressed);
        huff.decompressFile(hwCompressed, hwDecompressed, hwtree);

        // USConstitution example:
        String US = "inputs/USConstitution.txt";
        String USCompressed = US.substring(0, US.length()-4) + "_compressed.txt";
        String USDecompressed = US.substring(0, US.length()-4) + "_decompressed.txt";

        BinaryTree<CodeTreeElement> ustree = huff.makeCodeTree(huff.countFrequencies(US));
        Map<Character, String> usmap = huff.computeCodes(ustree);
        huff.compressFile(usmap, US, USCompressed);
        huff.decompressFile(USCompressed, USDecompressed, ustree);

        // War and Peace example:
        String wp = "inputs/WarAndPeace.txt";
        String wpCompressed = wp.substring(0, wp.length()-4) + "_compressed.txt";
        String wpDecompressed = wp.substring(0, wp.length()-4) + "_decompressed.txt";

        BinaryTree<CodeTreeElement> wptree = huff.makeCodeTree(huff.countFrequencies(wp));
        Map<Character, String> wpmap = huff.computeCodes(wptree);
        huff.compressFile(wpmap, wp, wpCompressed);
        huff.decompressFile(wpCompressed, wpDecompressed, wptree);

        //test 1: empty file
        String t1 = "inputs/emptyFile.txt";
        String t1Compressed = t1.substring(0, t1.length()-4) + "_compressed.txt";
        String t1Decompressed = t1.substring(0, t1.length()-4) + "_decompressed.txt";

        BinaryTree<CodeTreeElement> t1tree = huff.makeCodeTree(huff.countFrequencies(t1));
        Map<Character, String> t1map = huff.computeCodes(t1tree);
        huff.compressFile(t1map, t1, t1Compressed);
        huff.decompressFile(t1Compressed, t1Decompressed, t1tree);

        //test 2: 1 character file
        String t2 = "inputs/onechar.txt";
        String t2Compressed = t2.substring(0, t2.length()-4) + "_compressed.txt";
        String t2Decompressed = t2.substring(0, t2.length()-4) + "_decompressed.txt";

        BinaryTree<CodeTreeElement> t2tree = huff.makeCodeTree(huff.countFrequencies(t2));
        Map<Character, String> t2map = huff.computeCodes(t2tree);
        huff.compressFile(t2map, t2, t2Compressed);
        huff.decompressFile(t2Compressed, t2Decompressed, t2tree);

    }
}

