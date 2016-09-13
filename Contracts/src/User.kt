package bookyard.contracts.beans;

data class User(var id : Int = 0,
                var userName : String? = null,
                var fullName : String? = null,
                var email : String? = null,
                var appId : String? = null,
                var applicationTableId : Int = 0) {
}
