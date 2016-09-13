package bookyard.contracts.beans;

data class Book(var id : Int = 0,
                var name : String? = null,
                var author : String? = null,
                var description : String? = null,
                var amazonUrl : String? = null) {
}