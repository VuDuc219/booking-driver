package com.fpt.booking.domain.entities;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fpt.booking.domain.enums.AuthProvider;
import com.fpt.booking.domain.enums.DateAudit;
import com.fpt.booking.domain.enums.RoleName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class,
		  property = "id")
public class User extends DateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "phone", unique = true)
	private String phone;

	private String imageUrl;

	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

	private String providerId;

	@Column(name = "is_locked")
	private Boolean isLocked;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_mechanic")
	private Boolean isMechanic;

	private String address;

	private Integer otp;

	@Column(name = "total_money")
	private Double totalMoney;

	@Column(name = "credit_money")
	private Double creditMoney;

	private String accessToken;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy = "user")
	private Garage garage;

	@OneToOne(mappedBy = "user")
	private Moto moto;

	@OneToMany(mappedBy = "user")
	private List<FirebaseDevice> firebaseDevice;

	public User(String name,String phone,String email, String password, AuthProvider provider, Boolean isLocked, Boolean isActive) {
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.provider = provider;
		this.isLocked = isLocked;
		this.isActive = isActive;
		this.isMechanic = false;
	}

	public Set<RoleName> getRoleNames() {
		return roles.stream()
				.map(Role::getName)
				.collect(Collectors.toSet());
	}
}
