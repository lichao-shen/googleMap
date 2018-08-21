package co.uk.pshealth.googleMapCache.util;

public class Utils {
	
	
	/*
	 * This method split string array based on chunk size.
	 * String[] letters {"a","b","c","d","e","f","g","h"} with chunk size 3 will be split into
	 * [
	 * 	["a","b","c"],
	 *  ["d","e","f"],
	 *  ["g","h"]
	 * ] 
	 * 
	 */
	public static <T> String[][] splitStringArray(String[] array, int chunkSize) {
		
		int numOfChunks = (int)Math.ceil((double)array.length / chunkSize);
        String[][] output = new String[numOfChunks][];

        for(int i = 0; i < numOfChunks; ++i) {
            int start = i * chunkSize;
            int length = Math.min(array.length - start, chunkSize);

            String[] temp = new String[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;
        }

        return output;
	}
	

}
