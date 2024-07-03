package condfail

import rego.v1

p[k] := v if some v, k in input
