package policy

input_x_times_2 := input.x * 2

echo := input

default user_is_alice = false

user_is_alice {
    input.user == "alice"
}

hello := "Open Policy Agent"

