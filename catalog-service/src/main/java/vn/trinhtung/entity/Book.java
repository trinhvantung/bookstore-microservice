package vn.trinhtung.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String name;

	@Column(unique = true)
	private String slug;
	private String thumbnail;
	private Integer price;
	private Integer promotionPrice;
	private Integer quantity;

	@Column(columnDefinition = "text")
	private String description;

	private Integer totalPage;
	private String publisher;
	private Float weight;
	private Float length;
	private Float width;
	private Float height;
	
	private boolean enable;

	@Column(length = 4)
	private String publishingYear;

	@ManyToMany
	@JoinTable(
			name = "book_author", joinColumns = @JoinColumn(
					name = "book_id"
			), inverseJoinColumns = @JoinColumn(name = "author_id")
	)
	private List<Author> authors;

	@ManyToOne
	private Category category;
}
