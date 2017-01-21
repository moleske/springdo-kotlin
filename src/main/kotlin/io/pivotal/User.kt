package io.pivotal

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class User(
        internal var username: String,
        internal var password: String?,
        var email: String,
        @JsonIgnore @OneToMany(mappedBy = "user") var items: List<Item> = mutableListOf(),
        var isAdmin: Boolean = false,
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0L
) : UserDetails {

    class Operation : GrantedAuthority {
        override fun getAuthority(): String? = "ROLE_USER"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = arrayListOf(Operation())

    override fun getUsername(): String? = username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getPassword(): String? = password

    override fun toString(): String = username + email + isAdmin
}