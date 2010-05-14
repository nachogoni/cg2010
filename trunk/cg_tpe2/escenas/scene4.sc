
image {
   resolution 800 600
   aa 0 0
   samples 1
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 6.0
   p 4.0 10.0 10.0
}

camera {
   type pinhole
   eye 4.0 5.0 5.0
   target 0.0 0.0 0.0
   up -6.0 5.0 -5.0
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
   name mirror0
   type mirror
   refl { "sRGB nonlinear" 0 0 0 }
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
	 shader mirror0
	 type sphere
	 name sphere0
	 c 0 0 0
	 r 2
} 


object {
   shader phong1
   type plane
   p 0 0 0
   n 0 1 0
}

object {
   shader phong2
   type plane
   p 0 0 0
   n 1 0 0
}

object {
   shader phong3
   type plane
   p 0 0 0
   n 0 0 1
}

object { 
	 shader mirror0
	 type sphere
	 name sphere2
	 c 2 2 4
	 r 0.5
} 

object { 
	 shader mirror0
	 type sphere
	 name sphere3
	 c 4 2 2
	 r 0.5
} 
