package com.nokey.utils.android;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.nokey.utils.android.codegen.BaseCodeGenerator;
import com.nokey.utils.android.codegen.GeneratorFactory;

/**
 * TODO 默认配置保存到本地。
 * 
 * @author nokey
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 450;

	public MainFrame() {
		setBounds(100, 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("资源文件助手");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(0, 128, 128));
		setLayout(new BorderLayout());
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = (int) (MainFrame.this.getWidth() * WIDTH_RATIO);
				mLeftPanel.setPreferredSize(new Dimension(width, width));
			}
		});
		initLeftPanel();
		initCodeOutput();

	}

	private JLabel mGenSuffixCodeLabel;
	private static final String STRING_GEN_CODE_LABEL_2 = "代码: 主题style_2";

	public static final String TYPE_FUNCTION_RESOURCE = "切图批量重命名";
	public static final String TYPE_FUNCTION_CODE = "主题相关代码生成";
	private static final String STYLE_SUFFIX_DEFUALT = "_stk";

	private JPanel mLeftPanel;
	private JLabel mDestPathLabel;
	private JPanel mDestPanel;
	private JTextField mClassifySuffix;
	private JTextField mChildFolder;

	private JTextField mNameText;
	private JComboBox<String> mCodeTypeCombo;
	private JButton mFromFileBtn;
	private JButton mToFileBtn;
	private JButton mClearBtn;
	private JTextField mStyleSuffixText;
	private JCheckBox mStyleSuffixCb;

	private JPanel mScrollContentPanel;
	private JTextArea mAttrCodeTextArea;
	private JTextArea mStyle1CodeTextArea;
	private JTextArea mStyle2CodeTextArea;

	private String mCodeType = Constants.TYPE_CODE_DRAWABLE;
	private String mFunctionType = TYPE_FUNCTION_RESOURCE;
	private JPanel mResourceConfigPanel;
	private JPanel mCodeConfigPanel;

	private File mDestDir;
	private Map<String, JTextField> mRenameTextFields = new HashMap<String, JTextField>();
	private Map<String, List<File>> mFilesMap = new HashMap<>();
	private static final float WIDTH_RATIO = 0.45f;

	// 右侧代码生成窗口
	private void initCodeOutput() {
		mAttrCodeTextArea = new JTextArea();
		mStyle1CodeTextArea = new JTextArea();
		mStyle2CodeTextArea = new JTextArea();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel label1 = new JLabel("代码: 主题attr");
		label1.setForeground(Color.darkGray);
		label1.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel label2 = new JLabel("代码: 主题style_1");
		label2.setForeground(Color.darkGray);
		label2.setHorizontalAlignment(SwingConstants.LEFT);
		mGenSuffixCodeLabel = new JLabel(STRING_GEN_CODE_LABEL_2);
		mGenSuffixCodeLabel.setForeground(Color.darkGray);
		mGenSuffixCodeLabel.setHorizontalAlignment(SwingConstants.LEFT);

		JScrollPane scroll1 = new JScrollPane(mAttrCodeTextArea);
		JScrollPane scroll2 = new JScrollPane(mStyle1CodeTextArea);
		JScrollPane scroll3 = new JScrollPane(mStyle2CodeTextArea);
		panel.add(label1);
		panel.add(scroll1);
		panel.add(label2);
		panel.add(scroll2);
		panel.add(mGenSuffixCodeLabel);
		panel.add(scroll3);
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	private void initLeftPanel() {

		mLeftPanel = new JPanel();
		mLeftPanel.setLayout(new BoxLayout(mLeftPanel, BoxLayout.Y_AXIS));

		int width = (int) (MainFrame.this.getWidth() * WIDTH_RATIO);
		mLeftPanel.setPreferredSize(new Dimension(width, width));
		getContentPane().add(mLeftPanel, BorderLayout.WEST);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem(TYPE_FUNCTION_RESOURCE);
		comboBox.addItem(TYPE_FUNCTION_CODE);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem() == TYPE_FUNCTION_RESOURCE) {
					mFromFileBtn.setEnabled(true);
					mToFileBtn.setEnabled(true);
					mClearBtn.setEnabled(true);
					mResourceConfigPanel.setVisible(true);
					mCodeConfigPanel.setVisible(false);
					mFunctionType = TYPE_FUNCTION_RESOURCE;
					mCodeType = Constants.TYPE_CODE_DRAWABLE;
					if (mDestDir != null) {
						mDestPanel.setVisible(true);
					}

					mStyleSuffixCb.setEnabled(true);
					mStyleSuffixCb.setSelected(false);
					mStyleSuffixText.setEnabled(false);
				} else {
					mFromFileBtn.setEnabled(false);
					mToFileBtn.setEnabled(false);
					mClearBtn.setEnabled(false);
					mResourceConfigPanel.setVisible(false);
					mCodeConfigPanel.setVisible(true);
					mFunctionType = TYPE_FUNCTION_CODE;
					mCodeType = (String) mCodeTypeCombo.getSelectedItem();
					clearForResourceTemp();
					mDestPanel.setVisible(false);

					mStyleSuffixCb.setEnabled(false);
					mStyleSuffixCb.setSelected(true);
					mStyleSuffixText.setEnabled(true);
				}
			}
		});
		mLeftPanel.add(comboBox);
		mResourceConfigPanel = initConfigsResource();
		mCodeConfigPanel = initConfigsCode();
		mLeftPanel.add(mResourceConfigPanel);
		mLeftPanel.add(mCodeConfigPanel);
		mLeftPanel.add(initBtns());

		mDestPanel = new JPanel();
		mDestPathLabel = new JLabel();
		mDestPathLabel.setForeground(Color.blue);
		mDestPanel.add(mDestPathLabel);
		JButton openDestBtn = new JButton();
		openDestBtn.setText("打开");
		openDestBtn.setActionCommand(CMD_OPEN_DEST);
		openDestBtn.addActionListener(this);
		mDestPanel.add(openDestBtn);
		mDestPanel.setVisible(false);
		mLeftPanel.add(mDestPanel);

		JScrollPane scrollPanel = new JScrollPane();
		mScrollContentPanel = new JPanel();
		mScrollContentPanel.setLayout(new GridLayout(0, 1));
		scrollPanel.setViewportView(mScrollContentPanel);
		mLeftPanel.add(scrollPanel);
	}

	// 切图重命名功能的配置区域
	private JPanel initConfigsResource() {
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new GridLayout(2, 2));
		JLabel label1 = new JLabel("区分后缀:（以,分隔）");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		mClassifySuffix = new JTextField();
		mClassifySuffix.setColumns(10);
		mClassifySuffix.setText("@2x,@3x");
		topLeftPanel.add(label1);
		topLeftPanel.add(mClassifySuffix);

		JLabel label2 = new JLabel("对应子文件夹");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		mChildFolder = new JTextField();
		mChildFolder.setColumns(10);
		mChildFolder.setText("drawable-xhdpi,drawable-xxhdpi");
		topLeftPanel.add(label2);
		topLeftPanel.add(mChildFolder);

		return topLeftPanel;
	}

	// 代码生成的配置区域
	private JPanel initConfigsCode() {
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new GridLayout(2, 2));
		JLabel label1 = new JLabel("资源名称");
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		mNameText = new JTextField();
		mNameText.setColumns(10);
		mNameText.setText("color_theme_primary");
		topLeftPanel.add(label1);
		topLeftPanel.add(mNameText);

		JLabel label2 = new JLabel("类型");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		mCodeTypeCombo = new JComboBox<String>();
		mCodeTypeCombo.addItem(Constants.TYPE_CODE_COLOR);
		mCodeTypeCombo.addItem(Constants.TYPE_CODE_DRAWABLE);
		mCodeTypeCombo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				mCodeType = (String) e.getItem();

			}
		});

		topLeftPanel.add(label2);
		topLeftPanel.add(mCodeTypeCombo);
		topLeftPanel.setVisible(false);
		return topLeftPanel;
	}

	private JPanel initBtns() {
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(3, 2));

		mFromFileBtn = new JButton();
		mFromFileBtn.setText("导入文件/目录");
		Rectangle rect = new Rectangle(0, 0, 60, 30);
		mFromFileBtn.setBounds(rect);
		mFromFileBtn.setActionCommand(CMD_OPEN_FROM_FILE);
		mFromFileBtn.addActionListener(this);

		mToFileBtn = new JButton();
		mToFileBtn.setText("设置导出目录");
		mToFileBtn.setBounds(rect);
		mToFileBtn.setActionCommand(CMD_OPEN_TO_FILE);
		mToFileBtn.addActionListener(this);

		JButton startBtn = new JButton();
		startBtn.setText("开始生成");
		startBtn.setBounds(rect);
		startBtn.setActionCommand(CMD_START);
		startBtn.addActionListener(this);

		mClearBtn = new JButton();
		mClearBtn.setText("重置");
		mClearBtn.setBounds(rect);
		mClearBtn.setActionCommand(CMD_CLEAR);
		mClearBtn.addActionListener(this);

		btnPanel.add(mFromFileBtn);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		JLabel l = new JLabel("添加后缀  ");
		mStyleSuffixCb = new JCheckBox();
		mStyleSuffixText = new JTextField();
		mStyleSuffixText.setText(STYLE_SUFFIX_DEFUALT);
		mStyleSuffixText.setEnabled(false);
		mStyleSuffixCb.setSelected(false);
		mStyleSuffixCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (mStyleSuffixCb.isSelected()) {
					mStyleSuffixText.setEnabled(true);
				} else {
					mStyleSuffixText.setEnabled(false);
				}
			}
		});
		p.add(mStyleSuffixCb);
		p.add(l);
		p.add(mStyleSuffixText);
		btnPanel.add(p);
		btnPanel.add(mToFileBtn);
		btnPanel.add(startBtn);
		btnPanel.add(mClearBtn);
		btnPanel.add(new JPanel());

		return btnPanel;
	}

	private String[] getSuffixs() {
		String str = mClassifySuffix.getText();
		if (str != null) {
			return str.split(",");
		} else {
			return null;
		}
	}

	private String[] getChildDirs() {
		String str = mChildFolder.getText();
		if (str != null) {
			return str.split(",");
		} else {
			return null;
		}
	}

	private void startTask() {
		if (mFunctionType == TYPE_FUNCTION_RESOURCE) {
			new ResourceThread().start();
		} else {
			new CodeThread().start();
		}
	}

	private void buildItemPanels(Map<String, List<File>> map) {
		for (Entry<String, List<File>> entry : map.entrySet()) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JPanel filesPanel = new JPanel();
			filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
			panel.add(filesPanel);
			for (final File f : entry.getValue()) {
				JLabel nameLabel = new JLabel();
				nameLabel.setForeground(Color.blue);
				nameLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						FileUtils.openFolder(f);
					}
				});
				nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				nameLabel.setText(f.getName());
				filesPanel.add(nameLabel);

			}

			JPanel renamePanel = new JPanel();
			renamePanel.setLayout(new BoxLayout(renamePanel, BoxLayout.X_AXIS));
			panel.add(renamePanel);
			JLabel label = new JLabel();
			label.setText("重命名(不含后缀/扩展):");
			renamePanel.add(label);

			JTextField newNameText = new JTextField();
			Rectangle rect = new Rectangle(0, 0, 80, 25);
			newNameText.setBounds(rect);
			renamePanel.add(newNameText);
			mRenameTextFields.put(entry.getKey(), newNameText);
			panel.add(new JSeparator());
			mScrollContentPanel.add(panel);
		}
		getContentPane().validate();
	}

	private static final String CMD_OPEN_FROM_FILE = "CMD_OPEN_FROM_FILE";
	private static final String CMD_OPEN_TO_FILE = "CMD_OPEN_TO_FILE";
	private static final String CMD_START = "CMD_START";
	private static final String CMD_CLEAR = "CMD_CLEAR";
	private static final String CMD_OPEN_DEST = "CMD_OPEN_DEST";

	private class ResourceThread extends Thread {
		@Override
		public void run() {
			if (mFilesMap == null || mFilesMap.isEmpty() || mDestDir == null) {
				ToolsUtils.showMsgDialog("请选择输入文件和输出文件夹。");
			} else {
				StringBuilder strAttr = new StringBuilder();
				StringBuilder strStyle1 = new StringBuilder();
				StringBuilder strStyle2 = new StringBuilder();
				for (Entry<String, List<File>> entry : mFilesMap.entrySet()) {

					JTextField newNameText = mRenameTextFields.get(entry.getKey());
					if (newNameText != null) {
						String text = ToolsUtils.getTrimText(newNameText.getText());
						if (!text.isEmpty()) {
							String newNameSuffix = "";
							if (mStyleSuffixCb.isSelected()) {
								mStyle2CodeTextArea.setText(strStyle2.toString());
								newNameSuffix = ToolsUtils.getTrimText(mStyleSuffixText.getText());
							}
							copyAndRenameFiles(entry.getValue(), text + newNameSuffix);

							// 生成代码
							BaseCodeGenerator gen = getGenerator(newNameText.getText());
							if (gen != null) {
								strAttr.append(gen.genAttr());
								strStyle1.append(gen.genStyle1());
								strStyle2.append(gen.genStyle2());
							}
						}
					}

				}
				mAttrCodeTextArea.setText(strAttr.toString());
				mStyle1CodeTextArea.setText(strStyle1.toString());
				if (mStyleSuffixCb.isSelected()) {
					mStyle2CodeTextArea.setText(strStyle2.toString());
					String text = ToolsUtils.getTrimText(mStyleSuffixText.getText());
					if (!text.isEmpty()) {
						mGenSuffixCodeLabel
								.setText(STRING_GEN_CODE_LABEL_2 + "(后缀:" + mStyleSuffixText.getText() + ")");
					}
				}

			}
		}
	}

	private void copyAndRenameFiles(List<File> files, String newName) {
		String[] suffixArray = getSuffixs();
		String[] childDirArray = getChildDirs();

		for (File f : files) {
			boolean suitSuffix = false;
			for (int i = 0; i != suffixArray.length; i++) {
				if (f.getName().contains(suffixArray[i])) {
					suitSuffix = true;
					String destChildDir = mDestDir.getPath() + "/" + childDirArray[i];
					FileUtils.copyRename(f, FileUtils.checkCreateDir(destChildDir), newName);
				}
			}
			if (!suitSuffix) {
				FileUtils.copyRename(f, mDestDir, newName);
			}
		}
	}

	private class CodeThread extends Thread {
		@Override
		public void run() {
			BaseCodeGenerator gen = getGenerator(mNameText.getText());
			if (gen != null) {
				mAttrCodeTextArea.setText(gen.genAttr());
				mStyle1CodeTextArea.setText(gen.genStyle1());
				mStyle2CodeTextArea.setText(gen.genStyle2());
			}
		}
	}

	private BaseCodeGenerator getGenerator(String name) {
		String resName = name;
		String resType = mCodeType;
		String suffix = mStyleSuffixCb.isSelected() ? mStyleSuffixText.getText() : "";
		BaseCodeGenerator gen = GeneratorFactory.getGenerator(resType, resName, suffix);
		return gen;
	}

	private void clearForResourceTemp() {
		mScrollContentPanel.removeAll();
		mFilesMap.clear();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case CMD_OPEN_FROM_FILE:
			File[] files = FileUtils.selectFilesAndDir();
			if (files != null && files.length > 0) {
				clearForResourceTemp();
				mFilesMap = FileUtils.groupFilesBySuffix(files, getSuffixs());
				buildItemPanels(mFilesMap);
			}
			break;
		case CMD_OPEN_TO_FILE:
			mDestDir = FileUtils.selectDir();
			if (mDestDir != null) {
				mDestPathLabel.setText("导出到: " + mDestDir.getPath());
				mDestPanel.setVisible(true);
			}
			break;
		case CMD_START:
			startTask();
			break;
		case CMD_OPEN_DEST:
			if (mDestDir != null) {
				FileUtils.openFolder(mDestDir);
			} else {
				ToolsUtils.showMsgDialog("未选择目标文件夹。");
			}
			break;
		case CMD_CLEAR:
			clearForResourceTemp();
			validate();
			break;
		}

	}
}
