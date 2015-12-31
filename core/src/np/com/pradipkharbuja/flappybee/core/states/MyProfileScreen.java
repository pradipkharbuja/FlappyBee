package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;

/**
 * Created by Pradip Kharbuja on 12/29/2015.
 */
public class MyProfileScreen extends State {

    private Texture textureBackground;
    private Texture textureMyProfile;

    private TextButton btnLogin;
    private TextField txtFullName;
    private TextField txtEmail;
    private TextField txtPhoneNumber;

    private ScrollPane scrollPane;

    protected MyProfileScreen(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);

        textureBackground = new Texture("background.png");
        textureMyProfile = new Texture("my_profile_icon.png");

        Gdx.input.setInputProcessor(this);

        Skin skin = new Skin(Gdx.files.internal("uiskin"));

        Table table = new Table();
        table.defaults().width(this.getNewWidth()).height(70).pad(10);

        //table.setDebug(true);

        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);

        textFieldStyle.font.getData().padTop = 10;
        textFieldStyle.font.getData().padLeft = 120;
        textFieldStyle.font.getData().padRight = 100;
        textFieldStyle.font.getData().padTop = 10;

        txtFullName = new TextField("", skin);
        txtFullName.setStyle(textFieldStyle);
        txtFullName.setMessageText("Full Name");

        txtEmail = new TextField("", skin);
        txtEmail.setMessageText("Email Address");

        txtPhoneNumber = new TextField("", skin);
        txtPhoneNumber.setMessageText("Mobile (98XXXXXXXX)");

        btnLogin = new TextButton("Login", skin);
        btnLogin.getLabel().setFontScale(1.8f);

        btnLogin.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                saveData();
            }
        });

        table.add(txtFullName);
        table.row();
        table.add(txtEmail);
        table.row();
        table.add(txtPhoneNumber);
        table.row();
        table.add(btnLogin);

        scrollPane = new ScrollPane(table, skin);
        scrollPane.layout();

        scrollPane.setSize(this.getNewWidth() + 50, 400);
        scrollPane.setPosition(cam.position.x - 50, cam.position.y + 100);

        this.addActor(scrollPane);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        sb.draw(textureBackground, 0, 0);

        sb.draw(textureMyProfile, cam.position.x - textureMyProfile.getWidth() / 2, cam.position.y + 130);

        sb.end();

        this.act(Gdx.graphics.getDeltaTime());
        this.draw();
    }

    @Override
    public void dispose() {
        //do dispose all here
    }

    public void saveData() {

    }

    private float getNewWidth() {
        float width = 400; //(int) getWidth() - 200;
        return width;
    }
}
