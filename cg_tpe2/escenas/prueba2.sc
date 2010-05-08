image {
   resolution 800 600
   aa 0 2
   samples 4
}

camera {
   type thinlens
   eye 7.0 -6.0 5.0
   target 6.0 -5.0 4.0
   up -0.30 0.30 0.90
   fov 49.134 
   aspect 1.333 
   fdist 30.0 
   lensr 1.0
}

camera {
   type pinhole
   eye 7.0 -6.0 5.0
   target 6.0 -5.0 4.0
   up -0.30 0.30 0.90
   fov 49.134 
   aspect 1.333
}

camera {
   type pinhole
   eye 7.0 -6.0 5.0
   target 6.0 -5.0 4.0
   up -0.30 0.30 0.90
   fov 49.134 
   aspect 1.333
   shift 0.1 0.2
}

shader {
   name Mirror
   type mirror
   refl { "sRGB nonlinear" 0.800 0.800 0.800 }
}

object {
   shader Mirror
   type sphere
   name mirror1
   c -5 0 0
   r 3
}

object { 
   shader Mirror
   type generic-mesh 
   name meshName
   points 4 
      0 0 0
      2 0 0 
      0 2 0
      0 0 2
   triangles 2 
      1 2 3
      2 3 4
}

object {
   shader Mirror
   type plane
   p -7 0 0
   n 1 0 1
}

object {
   shader Mirror
   type plane
   p -7 0 0
   p 1 0 1
   p 2 2 2
}