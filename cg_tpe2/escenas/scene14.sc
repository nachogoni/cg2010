
image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 15.0 15.0 15.0
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
       0 1 3
       1 2 3
       0 4 5
       0 1 5
       1 5 2
       2 6 5
       4 5 7
       5 6 7
       0 4 7
       0 3 7
       2 3 6
       3 6 7
       8 9 11
       9 10 11
       8 12 13
       8 9 13
       9 13 10
       10 14 13
       12 13 15
       13 14 15
       8 12 15
       8 11 15
       10 11 14
       11 14 15
       16 17 19
       17 18 19
       16 20 21
       16 17 21
       17 21 18
       18 22 21
       20 21 23
       21 22 23
       16 20 23
       16 19 23
       18 19 22
       19 22 23   
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