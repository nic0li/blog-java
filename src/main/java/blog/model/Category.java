package blog.model;

import java.util.List;

import blog.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
	private List<Post> posts;

}
