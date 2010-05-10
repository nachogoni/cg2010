
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
   texture "earth.jpg"
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}

shader {
   name phong0
   type phong
   texture "earth.jpg"
   spec { "sRGB nonlinear" 1.0 1.0 1.0 } 50
   samples 4
}
object { 
	 shader phong0
	 type sphere
	 name sphere0
	 c -2.25 -2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere1
	 c -2.25 -2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere2
	 c -2.25 -2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere3
	 c -2.25 -2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere4
	 c -2.25 -0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere5
	 c -2.25 -0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere6
	 c -2.25 -0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere7
	 c -2.25 -0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere8
	 c -2.25 0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere9
	 c -2.25 0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere10
	 c -2.25 0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere11
	 c -2.25 0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere12
	 c -2.25 2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere13
	 c -2.25 2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere14
	 c -2.25 2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere15
	 c -2.25 2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere16
	 c -0.75 -2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere17
	 c -0.75 -2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere18
	 c -0.75 -2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere19
	 c -0.75 -2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere20
	 c -0.75 -0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere21
	 c -0.75 -0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere22
	 c -0.75 -0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere23
	 c -0.75 -0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere24
	 c -0.75 0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere25
	 c -0.75 0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere26
	 c -0.75 0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere27
	 c -0.75 0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere28
	 c -0.75 2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere29
	 c -0.75 2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere30
	 c -0.75 2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere31
	 c -0.75 2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere32
	 c 0.75 -2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere33
	 c 0.75 -2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere34
	 c 0.75 -2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere35
	 c 0.75 -2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere36
	 c 0.75 -0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere37
	 c 0.75 -0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere38
	 c 0.75 -0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere39
	 c 0.75 -0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere40
	 c 0.75 0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere41
	 c 0.75 0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere42
	 c 0.75 0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere43
	 c 0.75 0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere44
	 c 0.75 2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere45
	 c 0.75 2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere46
	 c 0.75 2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere47
	 c 0.75 2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere48
	 c 2.25 -2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere49
	 c 2.25 -2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere50
	 c 2.25 -2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere51
	 c 2.25 -2.25 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere52
	 c 2.25 -0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere53
	 c 2.25 -0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere54
	 c 2.25 -0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere55
	 c 2.25 -0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere56
	 c 2.25 0.75 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere57
	 c 2.25 0.75 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere58
	 c 2.25 0.75 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere59
	 c 2.25 0.75 2.25
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere60
	 c 2.25 2.25 -2.25
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere61
	 c 2.25 2.25 -0.75
	 r 0.7
} 
object { 
	 shader phong0
	 type sphere
	 name sphere62
	 c 2.25 2.25 0.75
	 r 0.7
} 
object { 
	 shader phong1
	 type sphere
	 name sphere63
	 c 2.25 2.25 2.25
	 r 0.7
} 
