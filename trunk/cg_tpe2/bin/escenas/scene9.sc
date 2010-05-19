
image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type pinhole
   eye 10.0 10.0 10.0
   target 0.0 0.0 0.0
   up 0.0 10.0 0.0
   fov 60 
   aspect 1.333
}

light {
   type point
   color { "sRGB nonlinear" 1 1 1 }
   power 3.0
   p 0.0 0.0 0.0
}

shader {
   name phong1
   type phong
   diff { "sRGB nonlinear" 0 0 1 }
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

object {
	shader phong1
	transform {
		scaleu 2.0
		rotatex 30
		rotatez 45	
		}
	type box
}
