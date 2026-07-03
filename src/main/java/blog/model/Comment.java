package blog.model;

import blog.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

	@Column(nullable = false)
	private String content;

	@ManyToOne
	private User user;

	@ManyToOne
	private Post post;

}
