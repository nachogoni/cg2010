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