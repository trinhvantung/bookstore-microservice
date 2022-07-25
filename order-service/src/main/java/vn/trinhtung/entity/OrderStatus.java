package vn.trinhtung.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum OrderStatus implements Serializable {
	NEW {
		@Override
		public String getDescription() {
			return "Đơn hàng mới";
		}
	},
	CANCELED {
		@Override
		public String getDescription() {
			return "Đã huỷ";
		}
	},
	PACKAGED {
		@Override
		public String getDescription() {
			return "Đã đóng gói";
		}
	},
	SHIPPING {
		@Override
		public String getDescription() {
			return "Đang giao";
		}
	},
	DELIVERED {
		@Override
		public String getDescription() {
			return "Đã nhận";
		}
	};

	public abstract String getDescription();

	public String getStatus() {
		return this.name();
	}
}
