
image {
   resolution 800 600
   aa 0 2
   samples 4
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 2.0
   p 0.5 0.5 10.0
}

camera {
   type pinhole
   eye 0.0 0.0 20.0
   target 0.0 0.0 0.0
   up 0.0 5.0 0.0
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
   name phong2
   type phong
   diff { "sRGB nonlinear" 1 0 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong3
   type phong
   diff { "sRGB nonlinear" 0 1 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong4
   type phong
   diff { "sRGB nonlinear" 1 1 0 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object { 
	 shader phong4
	 type sphere
	 transform {
	 	translate 0 0 3.3
	 	scalex 0.5
	 	rotatex 30
	 }
	 name sphere0
	 c 0 0 0
	 r 2
} 

object { 
	 shader phong4
	 type sphere
	 transform {
	 	scalex 2
	 }
	 name sphere0
	 c 0 0 0
	 r 2
}

object { 
	 shader phong3
	 type box
	 transform {
	 	scaleu 2
	 	translate 2 2 2
	 	rotatey 45
	 }
	 name box0
}