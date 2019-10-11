package cn.itechyou.cms.utils;

import java.util.ArrayList;
import java.util.List;

import cn.itechyou.cms.vo.Tree;

/**
 * 构建tree 
 * 
 */
public class TreeUtils {

	/**
	 * 
	 * @author kangxu2 2017-1-7
	 *
	 * @param nodes
	 * @return
	 */
	public static <T> Tree<T> build(List<Tree<T>> nodes) {

		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

		for (Tree<T> children : nodes) {

			String pid = children.getParentId();
			if (pid == null || "".equals(pid)) {
				topNodes.add(children);

				continue;
			}

			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setParent(true);
					parent.setChildren(true);

					continue;
				}
			}

		}

		Tree<T> root = new Tree<T>();
		if (topNodes.size() == 1) {
			root = topNodes.get(0);
		} else {
			root.setId("-1");
			root.setParentId("");
			root.setParent(false);
			root.setChildren(true);
			root.setChecked(true);
			root.setChildren(topNodes);
			root.setText("顶级节点");

		}

		return root;
	}

}