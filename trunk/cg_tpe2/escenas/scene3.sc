
image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 0.0 0.0 20.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

shader {
   name phong1
   type phong
   diff { "sRGB nonlinear" 0 0 1 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong0
   type phong
   diff { "sRGB nonlinear" 1 0 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object { 
   shader phong1 
   type generic-mesh 
   name mesh1
   points 24
      -1 1 1
      1 1 1
      1 -1 1
      -1 -1 1
      -1 1 -1
      1 1 -1
      1 -1 -1
      -1 -1 -1
      4 1 1
      6 1 1
      6 -1 1
      4 -1 1
      4 1 -1
      6 1 -1
      6 -1 -1
      4 -1 -1
      -4 1 1
      -6 1 1
      -6 -1 1
      -4 -1 1
      -4 1 -1
      -6 1 -1
      -6 -1 -1
      -4 -1 -1  
   triangles 36 
       1 2 4
       2 3 4
       1 5 6
       1 2 6
       2 6 3
       3 7 6
       5 6 8
       6 7 8
       1 5 8
       1 4 8
       3 4 7
       4 7 8
       9 10 12
       10 11 12
       9 13 14
       9 10 14
       10 14 11
       11 15 14
       13 14 16
       14 15 16
       9 13 16
       9 12 16
       11 12 15
       12 15 16
       17 18 20
       18 19 20
       17 21 22
       17 18 22
       18 22 19
       19 23 22
       21 22 24
       22 23 24
       17 21 24
       17 20 24
       19 20 23
       20 23 24   
}

object { 
	 shader phong0
	 type sphere
	 name sphere0
	 c -2.5 0 0
	 r 1
} 

object { 
	 shader phong0
	 type sphere
	 name sphere0
	 c 2.5 0 0
	 r 1
} 