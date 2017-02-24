/**
 * @author Name: Sarah Ruhl
 * 
 *  * Solution to Assignment 4 in CSE 373, Autumn 2016
 * University of Washington.
 * 
 * (Based on starter code by Steve Tanimoto.)
 * 
 * This program implements other programs given by another source such as ColorHash,
 * a HashTable made specifically for this program, ColorKey, which ColorHash 
 * uses as a key, ResponseItem, which is an object used to sort data in ColorHash.
 * This program creates an object, ComparePaintings, that can turn paintings into ColorHashes,
 * can compare to other ColorHashes to determine if they are the same painting, test itself, 
 * and create tables of collisions and similarities at various bits per pixels
 *
 */
public class ComparePaintings {
	
	private int bpp;
	public String probeType;// The type of collision resolution
	private int totalNumCol;

	// Post: Construct ComparePaintings object
	public ComparePaintings(){
		probeType = "Linear Probing";
		totalNumCol = 0;
	}  
	// Pre: Takes a filename and number of bits per pixels as parameters
	// Post: Constructs the hash table, count the colors.
	public ColorHash countColors(String filename, int bitsPerPixel) {
		bpp = bitsPerPixel;	
		ImageLoader hold = new ImageLoader(filename);
		int x = 0;// Initializing x-cordaniate
		int y = 0;// Initializing y-cordaniate
		ColorHash colorPalet = new ColorHash(3, bpp, probeType, .5);
		while(x<hold.getWidth()) {
			y = 0;
			while (y < hold.getHeight()) {
				ColorKey key = hold.getColorKey(x, y, bitsPerPixel);//
				ResponseItem holding = colorPalet.increment(key);//add collisions
				totalNumCol += holding.nCollisions;// Add the number of collisions
				// for this pixel to the total number of collisions
				y++;
			}
			x++;	
		}
		return colorPalet; // Change this to return the real result.
	}

	// Pre: Takes two hash tables of color counts. 
	// Post: compute a measure of similarity based on the cosine distance of two vectors.
	public double compare(ColorHash painting1, ColorHash painting2) {
		FeatureVector a = new FeatureVector(bpp);// Object used to get the colors out of ColorHash
		FeatureVector b = new FeatureVector(bpp);
		a.getTheCounts(painting1);
		b.getTheCounts(painting2);
		return a.cosineSimilarity(b);
	}

	//	Post: Test for the compare method. Should be 1.0, as it is comparing a painting against itself.
	public void basicTest(String filename) {
		ColorHash testHash = countColors(filename, bpp);
		double test123 = compare(testHash,testHash);
		if(test123 == 1.0){
			System.out.println("Success! The result was "+test123+"!");
		} else {
			System.out.println("Bad news! You got work to do. The result was"+ test123);
		}
	}

	//	Post: Compares the Mona Lisa, Starry Night and Christina's World to each other, using a variety of bits-per-pixel values,
	//  printing out a table of collision counts
	public void CollisionTests() {
			System.out.println("Bits Per Pixel C(Mona,linear) C(Mona,quadratic) C(Starry,linear) C(Starry,quadratic) "
																		+ "C(Christina,linear) C(Christina,quadratic)");
			for (int n = 24; n > 0; n = n-3) {// For various bpp sizes
				ComparePaintings mlLinear = new ComparePaintings();
				mlLinear.countColors("MonaLisa.jpg",n);				
				ComparePaintings starLinear = new ComparePaintings();				
				starLinear.countColors("StarryNight.jpg", n);
				ComparePaintings cwLinear = new ComparePaintings();
				cwLinear.countColors("ChristinasWorld.jpg", n);
				probeType = "Quadratic Probing";
				ComparePaintings mlQuad = new ComparePaintings();
				mlQuad.probeType = "Quadratic Probing";
				mlQuad.countColors("MonaLisa.jpg",n);
				ComparePaintings starQuad = new ComparePaintings();
				starQuad.probeType = "Quadratic Probing";
				starQuad.countColors("StarryNight.jpg", n);
				ComparePaintings cwQuad = new ComparePaintings();
				cwQuad.probeType = "Quadratic Probing";
				cwQuad.countColors("ChristinasWorld.jpg", n);
				System.out.println(n + "\t\t"+ mlLinear.totalNumCol+"\t\t"+mlQuad.totalNumCol+"\t\t "+starLinear.totalNumCol
									+"\t\t "+starQuad.totalNumCol+ "\t\t\t"+cwLinear.totalNumCol+"\t\t "+cwQuad.totalNumCol);
				this.totalNumCol = 0;
			}
	}
	
	// Post: Computes all combinations of Starry Night, Mona Lisa and Christina's Worlds' similarity at various amounts of 
	// bits per pixel and prints it out in a table
	public void fullSimilarityTest() {
		System.out.println("Bits Per Pixel       S(Mona,Starry)    S(Mona,Christina)     S(Starry,Christina)");
		for(int n = 24; n > 0; n = n - 3){
			ComparePaintings ml = new ComparePaintings();
			ComparePaintings star = new ComparePaintings();
			ComparePaintings chris = new ComparePaintings();
			ColorHash mlch = ml.countColors("MonaLisa.jpg", n);
			ColorHash starch = star.countColors("Starrynight.jpg", n);
			ColorHash chrisch = chris.countColors("ChristinasWorld.jpg",n);
			double m2s = ml.compare(mlch,starch);
			m2s = (Math.round(m2s*100000))/100000.0;
			double m2c = ml.compare(mlch,chrisch);
			m2c = Math.round(m2c*100000)/100000.0;
			double s2c = star.compare(starch, chrisch);
			s2c = Math.round(s2c*100000)/100000.0;
			System.out.println(n+"\t\t\t"+m2s+"\t\t\t"+m2c+"\t\t\t"+s2c);
		}
	}
		
	// Pre: Checks that the images can be loaded, so you don't have 
	// an issue with missing files or bad paths.
	public void imageLoadingTest() {
		ImageLoader mona = new ImageLoader("MonaLisa.jpg");
		ImageLoader starry = new ImageLoader("StarryNight.jpg");
		ImageLoader christina = new ImageLoader("ChristinasWorld.jpg");
		System.out.println("It looks like we have successfully loaded all three test images.");

	}
	/**
	 * This is a basic testing function, and can be changed.
	 */
	public static void main(String[] args) {
		ComparePaintings cp = new ComparePaintings();
		//cp.imageLoadingTest();
		cp.basicTest("MonaLisa.jpg");
		cp.CollisionTests();
		ColorHash c1 =cp.countColors("MonaLisa.jpg", 3);
		cp.fullSimilarityTest();
		try {
			ColorKey black = new ColorKey(0,6);//Tests a specific color
			long num = c1.getCount(black);
			System.out.println("Black appears "+num+ " times");
		}catch (Exception ColorKey) {
			System.out.print("That didn't work...");
		}
	}

}
