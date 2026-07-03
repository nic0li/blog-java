package blog.model;

import java.util.List;

import blog.common.model.BaseEntity;
import blog.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	private String name;

	private String photo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Post> posts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

}
