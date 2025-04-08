package policy

import rego.v1

input_x_times_2 := input.x * 2

echo := input

default user_is_alice := false

user_is_alice if {
	input.user == "alice"
}

hello := "Open Policy Agent"

makeAlternateSampleClass := {
	"nestedMap": input,
	"stringVal": "hello, test suite!",
}
