package ar.edu.itba.cg.tpe2.utils.noise;

import java.util.Random;

public class RandomNoise implements INoise {

	private int depth;
	
	private static int B = 512;
	private static int alpha = 2;
	private static int beta = 2;
	private static int N = 0x1000;
	private static int BM = 0xff;
	
	static final int p[] = new int[B + B + 2];
	static final double g1[] = new double[B + B + 2];
	static final double g2[][] = new double[B + B + 2][2];
	static final double g3[][] = new double[B + B + 2][3];
	
	static {
		int i, j, k;

		Random r = new Random();
		
		   for (i = 0 ; i < B ; i++) {
		      p[i] = i;
		      g1[i] = (double)((r.nextInt() % (B + B)) - B) / B;

		      for (j = 0 ; j < 2 ; j++)
		         g2[i][j] = (double)((r.nextInt() % (B + B)) - B) / B;
		      normalize2(g2[i]);

		      for (j = 0 ; j < 3 ; j++)
		         g3[i][j] = (double)((r.nextInt() % (B + B)) - B) / B;
		      normalize3(g3[i]);
		   }

		   while (--i > 0) {
		      k = p[i];
		      p[i] = p[j = Math.abs(r.nextInt() % B)];
		      p[j] = k;
		   }

		   for (i = 0 ; i < B + 2 ; i++) {
		      p[B + i] = p[i];
		      g1[B + i] = g1[i];
		      for (j = 0 ; j < 2 ; j++)
		         g2[B + i][j] = g2[i][j];
		      for (j = 0 ; j < 3 ; j++)
		         g3[B + i][j] = g3[i][j];
		   }
	}
	
	private static void normalize2(double v[])
	{
	   double s;

	   s = Math.sqrt(v[0] * v[0] + v[1] * v[1]);
	   v[0] = v[0] / s;
	   v[1] = v[1] / s;
	}

	private static void normalize3(double v[])
	{
	   double s;

	   s = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	   v[0] = v[0] / s;
	   v[1] = v[1] / s;
	   v[2] = v[2] / s;
	}
	
	public RandomNoise(int depth) {
		this.depth = depth;
		
	}
	
	@Override
	public int getDepth() {
		return this.depth;
	}

	@Override
	public float noise(float x, float y, float z) {
		   int i;
		   double val;
		   float sum = 0;
		   double p[] = new double[3], scale = 1;

		   p[0] = x;
		   p[1] = y;
		   p[2] = z;
		   for (i=0;i<this.getDepth();i++) {
		      val = noise3(p);
		      sum += val / scale;
		      scale *= alpha;
		      p[0] *= beta;
		      p[1] *= beta;
		      p[2] *= beta;
		   }
		   return(sum);
	}
	
	double noise2(double vec[])
	{
	   int bx0, bx1, by0, by1, b00, b10, b01, b11;
	   double rx0, rx1, ry0, ry1, q[] = new double[2], sx, sy, a, b, t, u, v;
	   int i, j;


	   //setup(0, bx0, bx1, rx0,rx1);
	   t = vec[0] + N;
	   bx0 = ((int)t) & BM;
	   bx1 = (bx0 + 1) & BM;
	   rx0 = t - (int)t;
	   rx1 = rx0 - 1;
	   //setup(1, by0, by1, ry0,ry1);
	   t = vec[1] + N;
	   by0 = ((int)t) & BM;
	   by1 = (by0 + 1) & BM;
	   ry0 = t - (int)t;
	   ry1 = ry0 - 1;
	   
	   i = p[ bx0 ];
	   j = p[ bx1 ];

	   b00 = p[ i + by0 ];
	   b10 = p[ j + by0 ];
	   b01 = p[ i + by1 ];
	   b11 = p[ j + by1 ];

	   sx = (rx0 * rx0 * (3 - 2 * rx0));
	   sy = (ry0 * ry0 * (3 - 2 * ry0));

	   q = g2[ b00 ] ; u = rx0 * q[0] + ry0 * q[1];
	   q = g2[ b10 ] ; v = rx1 * q[0] + ry0 * q[1];
	   a = u + sx * (v - u);

	   q = g2[ b01 ] ; u = rx0 * q[0] + ry1 * q[1];
	   q = g2[ b11 ] ; v = rx1 * q[0] + ry1 * q[1];
	   b = u + sx * (v - u);

	   return a + sy *(b - a);
	}


	private static double noise3(double vec[])
	{
	   int bx0, bx1, by0, by1, bz0, bz1, b00, b10, b01, b11;
	   double rx0, rx1, ry0, ry1, rz0, rz1, q[] = new double[3], sy, sz, a, b, c, d, t, u, v;
	   int i, j;

	   //setup(0, bx0,bx1, rx0,rx1);
	   t = vec[0] + N;
	   bx0 = ((int)t) & BM;
	   bx1 = (bx0 + 1) & BM;
	   rx0 = t - (int)t;
	   rx1 = rx0 - 1;
	   //setup(1, by0,by1, ry0,ry1);
	   t = vec[1] + N;
	   by0 = ((int)t) & BM;
	   by1 = (by0 + 1) & BM;
	   ry0 = t - (int)t;
	   ry1 = ry0 - 1;
	   //setup(2, bz0,bz1, rz0,rz1);
	   t = vec[2] + N;
	   bz0 = ((int)t) & BM;
	   bz1 = (bz0 + 1) & BM;
	   rz0 = t - (int)t;
	   rz1 = rz0 - 1;
	   
	   i = p[ bx0 ];
	   j = p[ bx1 ];

	   b00 = p[ i + by0 ];
	   b10 = p[ j + by0 ];
	   b01 = p[ i + by1 ];
	   b11 = p[ j + by1 ];

	   t  = (rx0 * rx0 * (3 - 2 * rx0));
	   sy = (ry0 * ry0 * (3 - 2 * ry0));
	   sz = (rz0 * rz0 * (3 - 2 * rz0));

	   q = g3[ b00 + bz0 ] ; u = rx0 * q[0] + ry0 * q[1] + rz0 * q[2];
	   q = g3[ b10 + bz0 ] ; v = rx1 * q[0] + ry0 * q[1] + rz0 * q[2];
	   a = u + t * (v - u);

	   q = g3[ b01 + bz0 ] ; u = rx0 * q[0] + ry1 * q[1] + rz0 * q[2];
	   q = g3[ b11 + bz0 ] ; v = rx1 * q[0] + ry1 * q[1] + rz0 * q[2];
	   b = u + t * (v - u);

	   c = a + sy * (b - a);

	   q = g3[ b00 + bz1 ] ; u = rx0 * q[0] + ry0 * q[1] + rz1 * q[2];
	   q = g3[ b10 + bz1 ] ; v = rx1 * q[0] + ry0 * q[1] + rz1 * q[2];
	   a = u + t * (v - u);

	   q = g3[ b01 + bz1 ] ; u = rx0 * q[0] + ry1 * q[1] + rz1 * q[2];
	   q = g3[ b11 + bz1 ] ; v = rx1 * q[0] + ry1 * q[1] + rz1 * q[2];
	   b = u + t * (v - u);

	   d = a + sy * (b - a);

	   return c + sz * (d - c);
	}

	@Override
	public float noise(float x, float y) {
		   int i;
		   double val;
		   float sum = 0;
		   double p[] = new double[2], scale = 1;

		   p[0] = x;
		   p[1] = y;
		   for (i=0;i<this.getDepth();i++) {
		      val = noise2(p);
		      sum += val / scale;
		      scale *= alpha;
		      p[0] *= beta;
		      p[1] *= beta;
		   }
		   return(sum);
	}

}
