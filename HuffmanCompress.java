import java.io.*;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author nikhilpande
 * @author nathanmcallister
 */
public class HuffmanCompress {

    private static PriorityQueue<BinaryTree<CodeTreeElement>> pq; // initialize priority queue to hold trees of letters and freq.
    BinaryTree<CodeTreeElement> finalTree; // initialize tree of all characters


    public String readFile(String pathName) throws IOException, IllegalArgumentException {
        BufferedReader input;
        String out = "", line;
        try { // Try reading it
            input = new BufferedReader(new FileReader(pathName));
            int c;
            while ((c = input.read()) != -1) {
                char ch = (char) c;
                out += ch;
            }
            input.close();
        }
        catch (FileNotFoundException e1) { // if it doesn't find the file at the given pathName
            System.err.println("Cannot open the file.\n" + e1.getMessage());
            return out;
        }

        return out;
    }

    /**
     * Read file provided in pathName and count how many times each character appears
     * @param pathName - path to a file to read
     * @return - Map with a character as a key and the number of times the character appears in the file as value
     * @throws IOException
     */
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        String page = readFile(pathName);

        Map<Character, Long> freq = new TreeMap<Character, Long>();
        char[] letters = page.toCharArray(); // take characters in string and make into an array

        // Loop over all characters in string, either incrementing count or adding in
        for (char c: letters) {

            // Checking if character is already there
            if (freq.containsKey(c)) {
                // if so, increase frequency count
                freq.put(c, freq.get(c) + 1);
            } else {
                // if not, insert character as key with freq of 1
                freq.put(c, 1L);
            }
        }
        return freq;
    }

    /**
     * Construct a code tree from a map of frequency counts. Note: this code should handle the special
     * cases of empty files or files with a single character.
     *
     * @param frequencies a map of Characters with their frequency counts from countFrequencies
     * @return the code tree.
     */
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies) throws IllegalArgumentException {

        try {
            // initialize priority queue, passing in the new comparator
            pq = new PriorityQueue<>(frequencies.size(), new TreeComparator<CodeTreeElement>());
        }
        catch (IllegalArgumentException e) { // if there's nothing in the file, can't have pq of size 0
            return null;
        }
        // for every character in the treemap...
        for (Character key : frequencies.keySet()) {
            // create new binary tree with only one CodeTreeElement, which holds frequency and character data extracted from map
            BinaryTree<CodeTreeElement> ch = new BinaryTree<CodeTreeElement>(new CodeTreeElement(frequencies.get(key), key));
            // and add this tree to the priority queue. Since we passed in our comparator that checks its frequency,
            // we know that the key will be its frequency.
            pq.add(ch);
        }

        while (pq.size() > 1) { // until there's only one tree left, which will be the big tree of all chars
            // remove the elements that have the two lowest frequencies
            BinaryTree<CodeTreeElement> e1 = pq.remove();
            BinaryTree<CodeTreeElement> e2 = pq.remove();

            // create an element whose frequency is the total of the two removed. Note that its character data doesn't matter.
            CodeTreeElement head = new CodeTreeElement(e1.getData().getFrequency() + e2.getData().getFrequency(), null);
            // create a new tree whose head is this consolidated element and whose children are e1 and e2
            BinaryTree<CodeTreeElement> parent = new BinaryTree<CodeTreeElement>(head, e1, e2);
            pq.add(parent); // and add to priority queue
        }

        finalTree = pq.peek();
        return finalTree; // by the end, the only thing left will be the big tree, so return that
    }

    /**
     * Computes the code for all characters in the tree and enters them
     * into a map where the key is a character and the value is the code of 1's and 0's representing
     * that character.
     *
     * @param codeTree the tree for encoding characters produced by makeCodeTree
     * @return the map from characters to codes
     */
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        // initialize map of characters and their corresponding codes
        Map<Character, String> codeMap = new TreeMap<Character, String>();
        String code = "";

        if (codeTree == null) { // nothing in tree
            return codeMap;
        }
        else if (codeTree.size() == 1) { // only one character in tree, put it in map and set code to 1
            codeMap.put(codeTree.getData().getChar(), "1");
        }
        else {
            codeHelper(codeMap, finalTree, code);
        }
        return codeMap;
    }

    /**
     * helper method for computeCodes that recursively goes through the tree and makes the map
     *
     * @param codes map of characters and codes
     * @param tree tree we are currently working on
     * @param s string to keep the code
     */
    public void codeHelper(Map<Character, String> codes, BinaryTree<CodeTreeElement> tree, String s) {
        if (tree.isLeaf()) { // first check if it's a leaf: if so, it's a letter, so put it in map with its corresponding code
            codes.put(tree.getData().getChar(), s);
        }
        if (tree.hasLeft()) {
            codeHelper(codes, tree.getLeft(), s+"0"); // move to left child and add 0 to string
        }
        if (tree.hasRight()) {
            codeHelper(codes, tree.getRight(), s+"1"); // move to right child and add 1 to string
        }
    }

    /**
     * Compress the file pathName and store compressed representation in compressedPathName.
     * @param codeMap - Map of characters to codes produced by computeCodes
     * @param pathName - File to compress
     * @param compressedPathName - Store the compressed data in this file
     * @throws IOException
     */
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {
        try {
            BufferedReader input = new BufferedReader(new FileReader(pathName));
            BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);
            int c;
            while ((c = input.read()) != -1) { // until the end of the file
                char ch = (char) c;
                String bitCode = codeMap.get(ch);
                for (char x : bitCode.toCharArray()) { // for each bit code write 0 and 1 as false and true
                    if (x == '0') {
                        bitOutput.writeBit(false);
                    } else if (x == '1') {
                        bitOutput.writeBit(true);
                    }
                }
            }
            // close files
            input.close();
            bitOutput.close();
        }
        catch (IOException e) {
            System.err.println("Cannot find file: Compressing\n" + e.getMessage());

        }
    }

    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));
            BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
            BinaryTree<CodeTreeElement> current = codeTree; // to keep track of element we're at. Initialize it at head of tree.

            while (bitInput.hasNext()) {
                boolean bit = bitInput.readBit(); // reads next bit

                if (bit) {
                    if (codeTree.isLeaf()) { // if there's only one thing in the whole tree
                        output.write(current.getData().getChar());
                    } else if (current.getRight().isLeaf()) { // reach down and get the child, write it in
                        current = current.getRight();
                        output.write(current.getData().getChar());
                        current = codeTree; // set current back to head
                    } else {
                        current = current.getRight(); // if no leaves in sight, move down the tree to the right
                    }

                } else {
                    current = current.getLeft(); // move down to left
                    if (current.isLeaf()) { // if it's a leaf, write character and set current back to top
                        output.write(current.getData().getChar());
                        current = codeTree;
                    }
                }
            }

            // close files
            output.close();
            bitInput.close();
        }
        catch (IOException e) {
            System.err.println("Cannot find file: Decompressing\n" + e.getMessage());
        }
    }
}

